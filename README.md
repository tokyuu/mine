# DV.R
주마다 갱신되는 빌보드 차트 100위를 rvest를 이용해 스크래핑해 현재 순위, 제목, 아티스트 이름, 지난주 순위, 최고 순위 데이터를 모았으며, 장르와 발매시기와 같은 정보는 해당 사이트에 없어 rselenium을 사용해 구글로 검색해서 데이터를 모았다. 그러나 rselenium을 사용해 검색 시 구글에 감지되어 검색이 중단되는 일이 발생해 검색 중간중간에 휴식시간을 주어 로봇으로 인식되는 일을 최대한 피하도록 했다. 빌보드 차트 순위는 일주일마다 바뀌므로 저장하는 파일의 이름은 사이트에 게시되어 있는 현재 주의 일요일의 날짜로 하였다.
이후 저장한 데이터를 이용해 장르의 분포를 보여주는 원그래프를 만들었고, 상위 10%의 노래의 순위변동을 점 그래프로 표현했고, 최고 순위 분포를 꺾은선 그래프로 그렸다.
이 코드를 정기적으로 실행하기 위해 마지막 4줄에 있는 패키지를 이용해 스케쥴러를 열어 일주일마다 실행하도록 지정했다.
