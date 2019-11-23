library(rvest)

url1='https://store.steampowered.com/games/#p='
url2='&tab=NewReleases'
page=0:2
pages=paste0(url1,page,url2)

extract = function(page_url) {
  html <- read_html(page_url)
  node <- html %>% html_nodes("#NewReleasesRows")
  node = node %>% html_nodes('a')
  links = node %>% html_attr('href')
  
  c1=vector("character",0)
  c2=vector("character",0)
  for(i in 1:length(links)){
    game_html = read_html(links[i])
    node2 = game_html %>% html_nodes('.apphub_AppName')
    game_name = node2 %>% html_text()
    c1=append(c1,game_name)
    
    node3 = game_html %>% html_nodes('.glance_tags.popular_tags')
    node3 = node3 %>% html_nodes('a')
    tags = node3 %>% html_text()
    tags = gsub('\\t',"",tags)
    tags = gsub('\\n',"",tags)
    tags = gsub('\\r',"",tags)
    tags = paste(tags,collapse = ", ")
    c2=append(c2,tags)
  }
  m=cbind(c1,c2)
}

mat=extract(pages[1])
for(i in 2:length(page) ) mat <- rbind(mat, extract(pages[i]))