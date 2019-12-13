library(rvest)
library(RSelenium)
require(lubridate)
library(stringr)

url='https://www.billboard.com/charts/hot-100'
pages=url

html <- read_html(url)
folder <- 'd:/billboard'
if(!dir.exists(folder)) dir.create(folder)
setwd(folder)
node <- html %>% html_nodes(".date-selector__button.button--link")
date = node %>% html_text()
date = str_trim(date)
file.name = paste0(date,'.html')
write_xml(html, file = file.name)

extract = function(page_url) {
  html <- read_html(page_url) 
  
  node <- html %>% html_nodes(".chart-list__elements") # ol
  node2 = node %>% html_nodes(".chart-list__element.display--flex") # ol안의 list들
  
  node3 = node2 %>% html_nodes(".chart-element__rank__number") # 현재 랭크
  rank = node3 %>% html_text()
  
  node4 = node2 %>% html_nodes(".chart-element__information__song.text--truncate.color--primary") # 제목
  title = node4 %>% html_text()
  
  node5 = node2 %>% html_nodes(".chart-element__information__artist.text--truncate.color--secondary") # 아티스트 이름
  artist = node5 %>% html_text()
  
  node6 = node2 %>% html_nodes(".chart-element__meta.text--center.color--secondary.text--last") # 지난주 랭크
  lastweek_rank = node6 %>% html_text()
  
  node7 = node2 %>% html_nodes(".chart-element__meta.text--center.color--secondary.text--peak") # 최고 랭크
  peak_rank = node7 %>% html_text()
  
  m=cbind(rank,title,artist,lastweek_rank,peak_rank)
}

mat=extract(file.name)

remDr <- remoteDriver(
  remoteServerAddr = "localhost",
  port = 4446L,
  browserName = "chrome"
)
remDr$open()

extract2 = function() {
  genre=vector("character",0)
  release=vector("character",0)
  country=vector("character",0)
  remDr$navigate("http://www.google.com")
  for(i in 1:100){
    if(i==51){Sys.sleep(300)}
    if(i==90){Sys.sleep(300)}
    webElem <- remDr$findElement(using = "css", "[name = 'q']")
    if(i>1){webElem$clearElement()}
    webElem$sendKeysToElement(list(paste(mat[i,2],mat[i,3]), key = "enter")) # 제목 검색
    
    webElems = remDr$findElements(using = "css selector", "span.LrzXr.kno-fv") # 내용들
    webElems2 = remDr$findElements(using = "css selector", "span.w8qArf") # 분류이름들
    pos=0
    pos2=0
    if(length(webElems)!=0){
      for(j in 1:length(webElems)){
        if(unlist(webElems2[[j]]$getElementText())=="장르:"){
          pos=j
        }
        if(unlist(webElems2[[j]]$getElementText())=="발매일:"){
          pos2=j
        }
      }
    }
    if(pos!=0){genre = append(genre, unlist(webElems[[pos]]$getElementText()))}
    else{genre = append(genre, "-")}
    if(pos2!=0){release = append(release, unlist(webElems[[pos2]]$getElementText()))}
    else{release = append(release, "-")}
  }
  m=cbind(genre,release)
}
mat2=extract2()
mat=cbind(mat,mat2)

remDr$close() 

#pie chart
library(ggplot2)
library(RColorBrewer)
#display.brewer.all()
#https://www.r-bloggers.com/color-palettes-in-r/
mf = data.frame(mat)
circle = table(mf$genre)
rownames(circle)[1]="기타"
rownames(circle)[6]="R&B/트랩"
library(plotly)
circle.df=data.frame(circle)
colnames(circle.df)=c("genre","value")
plot_ly(circle.df, labels = ~genre, values = ~value, type = 'pie')

for(i in 1:100){if(mat[i,4]=='-'){mat[i,4]=0}}
r_d = as.numeric(mat[,4])-as.numeric(mat[,1]) #지난주와 이번주 순위 차
max5 = sort(r_d,decreasing = T)[1:5] #큰값5개
min5 = sort(r_d,decreasing = T)[96:100] #작은값5개
r_d.index = vector("numeric",0) #큰값5개의 인덱스
for(i in 1:5){r_d.index=c(r_d.index,which(r_d==max5[i],arr.ind = TRUE))}
r_d.index2 = vector("numeric",0) #작은값5개의 인덱스
for(i in 1:5){r_d.index2=c(r_d.index2,which(r_d==min5[i],arr.ind = TRUE))}
mat[r_d.index,] #순위가 많이 상승한 노래 5개
mat[r_d.index2,] #순위가 많이 떨어진 노래 5개

#dot chart
library(ggplot2)
this_week=rep("this week",10)
this_week=data.frame(this_week)
this_week=cbind(mf[1:10,c(2,1)],this_week)
colnames(this_week)[3]="state"
last_week=rep("last week",10)
last_week=data.frame(last_week)
last_week=cbind(mf[1:10,c(2,4)],last_week)
colnames(last_week)[2]="rank"
colnames(last_week)[3]="state"
data=rbind(this_week,last_week)

data$rank=as.numeric(levels(data$rank))[data$rank]
data$title <- factor(data$title,levels=rev(unique(data$title))) #그래프에서 타이틀을 위에서부터 순서대로 정렬하기 위해 정렬함

ggplot(data, aes(rank, title)) +
  geom_line(aes(group = title)) +
  geom_point(aes(color = state)) +
  scale_x_continuous(limits = c(1, max(data$rank))) +
  scale_x_reverse() +
  ggtitle("상위 10위의 순위 변동") + 
  theme(plot.title = element_text(family = "serif", face = "bold", hjust = 0.5, size = 15)) +
  geom_text(aes(label = rank, color=state), size = 3, vjust = 1)

#꺾은선그래프
peak.data=table(mf$peak_rank)
peak.data=data.frame(peak.data)
colnames(peak.data)=c("PeakRank","num")
peak.data$PeakRank=as.numeric(peak.data$PeakRank)
ggplot(data = peak.data, aes(PeakRank, num, group=1)) +
  geom_line(linetype="dashed", color="skyblue") +
  geom_point(color="skyblue") +
  scale_x_reverse() +
  ggtitle("최고 순위 분포")  + 
  theme(plot.title = element_text(family = "serif", face = "bold", hjust = 0.5, size = 15)) +
  geom_text(aes(label = num), size = 3, vjust = -1.5, color="cyan3")

library(data.table)
library('knitr')
library('miniUI')
library('shiny')
library(taskscheduleR)
# https://stackoverflow.com/questions/2793389/scheduling-r-script