library(RSelenium)

remDr <- remoteDriver(
  remoteServerAddr = "localhost",
  port = 4446L,
  browserName = "chrome"
)

remDr$open()

remDr$navigate("http://www.google.com")
webElem <- remDr$findElement(using = "css", "[name = 'q']")
webElem$sendKeysToElement(list("asset store", key = "enter"))#에셋 스토어 검색

webElems <- remDr$findElements(using = "class", "r")
resHeaders <- unlist(lapply(webElems, function(x) {x$getElementText()}))
resHeaders

webElem <- webElems[[1]]#첫번째에 있는 검색창 클릭
webElem$clickElement()
remDr$getCurrentUrl()

webElems = remDr$findElement(using = "class", "_32RN3")#메인화면에 있는 블랙프라이데이 선택
webElem = webElems
webElem$clickElement()

extract = function() {
  name=vector("character",0)#상품 이름
  reviewnum=vector("character",0)#후기 수
  discounted_price=vector("character",0)#할인가
  price=vector("character",0)#정가
  for(i in 1:24){#페이지에 24개의 상품이 존재
    p1="//*[@id='main-layout-scroller']/div/div[1]/div/div/div/div/div[1]/div[2]/div[3]/div/div["
    p2="]/div/div/div/div/a[2]"
    p=paste0(p1,i,p2)#상품들 있는곳까지의 xpath
    
    n_p=paste0(p,"/div[1]/div")#이름 있는 경로
    webElems = remDr$findElement(using = "xpath", n_p)
    if(unlist(webElems$getElementText())==""){#이름이 없을시 다른 경로로 찾음
      n_p=paste0(p,"/div[1]/div[2]")
      webElems = remDr$findElement(using = "xpath", n_p)
    }
    name=append(name,unlist(webElems$getElementText()))
    
    r_p=paste0(p,"/div[2]/div/div[6]")#후기 수가 있는 경로
    webElems = remDr$findElement(using = "xpath", r_p)
    tmp=unlist(webElems$getElementText())
    tmp=gsub('\\(',"",tmp)#(,) 뗌
    tmp=gsub('\\)',"",tmp)
    reviewnum=append(reviewnum,tmp)
    
    d_p=paste0(p,"/div[3]/div/div/div[1]")#할인가 있는 경로
    webElems = remDr$findElement(using = "xpath", d_p)
    discounted_price=append(discounted_price,unlist(webElems$getElementText()))
    
    p_p=paste0(p,"/div[3]/div/div/div[2]")#정가가 있는 경로
    webElems = remDr$findElement(using = "xpath", p_p)
    price=append(price,unlist(webElems$getElementText()))
  }
  m=cbind(name,reviewnum,discounted_price,price)
}
mat=extract()
mat

remDr$close() 
