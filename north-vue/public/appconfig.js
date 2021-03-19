
// var BASE_URL = "http://127.0.0.1:80/"
var BASE_URL = null
var BASE_WS_URL = null
var htmlTitle = "后台管理系统"


window.title = htmlTitle
document.getElementsByTagName("title")[0].innerText = htmlTitle;

if(!BASE_URL){
    BASE_URL = "http://127.0.0.1:80/"
}
if(!BASE_WS_URL){
    BASE_WS_URL = BASE_URL.startsWith("https")?BASE_URL.replace("https","wss"):BASE_URL.replace("http","ws")
}

window.BASE_URL = BASE_URL
window.BASE_WS_URL = BASE_WS_URL
