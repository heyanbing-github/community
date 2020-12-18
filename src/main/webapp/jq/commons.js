var paramStr = window.location.search;
if(paramStr){
    paramStr = paramStr.substring(1,paramStr.length);
}
var param = [];
var arr = paramStr.split("&");
for(var index in arr){
    var p = arr[index].split("=");
    param[p[0]] = p[1];
}

/**
 * 获取请求参数
 * @param name
 * @returns {*}
 */
function getParameter(name){
    return param[name];
}