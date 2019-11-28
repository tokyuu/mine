library(RSelenium)

remDr <- remoteDriver(
  remoteServerAddr = "localhost",
  port = 4446L,
  browserName = "chrome"
)

remDr$open()

remDr$navigate("http://www.google.com")
webElem <- remDr$findElement(using = "css", "[name = 'q']")
webElem$sendKeysToElement(list("asset store", key = "enter"))#���� ����� �˻�

webElems <- remDr$findElements(using = "class", "r")
resHeaders <- unlist(lapply(webElems, function(x) {x$getElementText()}))
resHeaders

webElem <- webElems[[1]]#ù��°�� �ִ� �˻�â Ŭ��
webElem$clickElement()
remDr$getCurrentUrl()

webElems = remDr$findElement(using = "class", "_32RN3")#����ȭ�鿡 �ִ� ���������̵��� ����
webElem = webElems
webElem$clickElement()

extract = function() {
  name=vector("character",0)#��ǰ �̸�
  reviewnum=vector("character",0)#�ı� ��
  discounted_price=vector("character",0)#���ΰ�
  price=vector("character",0)#����
  for(i in 1:24){#�������� 24���� ��ǰ�� ����
    p1="//*[@id='main-layout-scroller']/div/div[1]/div/div/div/div/div[1]/div[2]/div[3]/div/div["
    p2="]/div/div/div/div/a[2]"
    p=paste0(p1,i,p2)#��ǰ�� �ִ°������� xpath
    
    n_p=paste0(p,"/div[1]/div")#�̸� �ִ� ���
    webElems = remDr$findElement(using = "xpath", n_p)
    if(unlist(webElems$getElementText())==""){#�̸��� ������ �ٸ� ��η� ã��
      n_p=paste0(p,"/div[1]/div[2]")
      webElems = remDr$findElement(using = "xpath", n_p)
    }
    name=append(name,unlist(webElems$getElementText()))
    
    r_p=paste0(p,"/div[2]/div/div[6]")#�ı� ���� �ִ� ���
    webElems = remDr$findElement(using = "xpath", r_p)
    tmp=unlist(webElems$getElementText())
    tmp=gsub('\\(',"",tmp)#(,) ��
    tmp=gsub('\\)',"",tmp)
    reviewnum=append(reviewnum,tmp)
    
    d_p=paste0(p,"/div[3]/div/div/div[1]")#���ΰ� �ִ� ���
    webElems = remDr$findElement(using = "xpath", d_p)
    discounted_price=append(discounted_price,unlist(webElems$getElementText()))
    
    p_p=paste0(p,"/div[3]/div/div/div[2]")#������ �ִ� ���
    webElems = remDr$findElement(using = "xpath", p_p)
    price=append(price,unlist(webElems$getElementText()))
  }
  m=cbind(name,reviewnum,discounted_price,price)
}
mat=extract()
mat

remDr$close() 