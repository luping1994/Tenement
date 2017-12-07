if ($("#openType").val() == "alarm") {
    $("#btnAlarm").trigger("click");
}


//创建一张图片
function createImage(data) {
    var imageGroup = d3.select("svg")
        .append("g")
        .classed("ele", true)
        .data([data]);

    imageGroup.append("image")
        .classed("shape-image", true)
        .attr("x", function (d) {
            return d.x
        })
        .attr("y", function (d) {
            return d.y
        })
        .attr("width", function (d) {
            return d.width
        })
        .attr("height", function (d) {
            return d.length
        })
        .attr("xlink:href", function (d) {
            if (d.status)
                return d.openUrl;
            return d.closeUrl;
        });


    // imageGroup.attr("transform", function (d) {
    //     return "rotate(" + d.radius + "," + d.x2 + "," + d.y2 + ")";
    // });

    imageGroup.on("click", openConfirmDialog);

}

//初始化容器
function initContainer() {
    $.ajax({
        url: 'http://192.168.2.234:8080/JsonServlet',
        // url: 'http://tit.suntrans-cloud.com/api/v1/home/floor_plan',
        // data: {'ruleId':$("#ruleId").val(),'dtuSn':$("#dtuSn").val()},
        type: 'POST',
        dataType: "json",
        headers: {
            'Authorization': token
        },
        data: {'house_id': house_id},
        success: function (json) {
            //初始化容器样式
            if (json.result) {
                var con = json.container;
                if (con) {
                    width = con.width;
                    height = con.height;
                    $("div.wrapper-position").css("width", con.width);
                    $("div.wrapper-position").css("height", con.height);
                    $("svg.designer").css("width", con.width);
                    $("svg.designer").css("height", con.height);
                    $("svg.designer").css("background-color", con.bgColor);
                    $("svg.designer").css("background-image", "url(" + con.bgImage + ")");
                    $("svg.designer").empty();
                    $("body").css("background-color", con.bgColor);
                }
                // json.signals.map(function(signal){
                //  if(signal){
                // 	 signalMap[''+signal.id]=signal;
                //  }
                // });
                json.elements.map(createElement);
            }
        }
    });
    // refreshAlarm();
//    console.log("初始化请求")

//        $.ajax({
//            url: 'http://tit.suntrans-cloud.com/api/v1/home/floor_plan',
//            // data: {'ruleId':$("#ruleId").val(),'dtuSn':$("#dtuSn").val()},
//            type: 'POST',
//            dataType: "json",
//            headers: {
//                'Authorization': 'Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6ImZjYTZjZGQ2NTU5ODU2MjEzNzczMDI0YzE1YzZkN2FkMmZhMWM3OTA5Mzc0MzhkYzgyOWZmZWRhM2I2NjliMmFkYWMyMjlkZTRmYWRkZGQyIn0.eyJhdWQiOiIyIiwianRpIjoiZmNhNmNkZDY1NTk4NTYyMTM3NzMwMjRjMTVjNmQ3YWQyZmExYzc5MDkzNzQzOGRjODI5ZmZlZGEzYjY2OWIyYWRhYzIyOWRlNGZhZGRkZDIiLCJpYXQiOjE1MTEzOTk2NDksIm5iZiI6MTUxMTM5OTY0OSwiZXhwIjoxNTExOTE4MDQ5LCJzdWIiOiI5Iiwic2NvcGVzIjpbXX0.wBprh2XtOBWfugZBlk4X_dlCoJpiQaV80XR40YbB-klqFZ-z6eW431LR1o7HXBvqwD1D1i-K9H5K0sgyF9yoV8UXEThMV3tS1iKbeO2S_1THfUd-m46ym9cxK8tQQku2AC7c0-W1-LzdlfsvOsLhm7SDETjIQa6fk-5VH0QX5939Z7gebmkeXMHgy2zXTg28Mfr-DnNXSauiBbWq6ZWFv8oD9e0MT7MyDKi46j11inmYwn06GQuJxNS57wPxz24psqe4q86q1ljOv0G20aEkmQKuPfPWL5rIBEXjtbECyz3DN9bO7HVHva0YqwMVftf4ScZT3gOdDZjkS6HZq5R3kSeZY2S6Oyrw_PgewFTuBrGIzQA3NBsYXcvxq_rECwr8UC5HAZLAsK94yrnGg41v9f09vU5sZq1joKUjQ-V8YMGIpectNZNGVCc15zeIlLW3TiBKPOXmrbR1eTmvihVK5RyVB0LViWXdR5_xWWVHT4QzRZF7MdMPypTyvEeID-KQHcXlB2HD1lvS-7xrYZIJC-Fuq933vEOdos2Y1teFqu314gBFDp8T0SpMcWQT7FMnEuRcuJnv_rNZ8muhkCsNtwZYwoar5wZtRx8Duzt5dCtHpEwNVpBRKUnS9Nbdq21F34_4RYmwxsYUlCmzgAeBKtcXKSygKR3WCpOdbXUJ098',
//            },
//            data: {'house_id': '1'},
//            success: function (json) {
//                console.log(json)
//            }
//        });
}

//刷新数据
function refreshContainer() {
//        $.ajax({
//            url: 'http://192.168.2.234:8080/JsonServlet',
//            // data: {'ruleId':$("#ruleId").val(),'dtuSn':$("#dtuSn").val()},
//            type: 'GET',
//            dataType: "json",
//            success: function (json) {
//                //初始化容器样式
//                if (json.result) {
//                    var con = json.container;
//                    if (con) {
//                        width = con.width;
//                        height = con.height;
//                        $("div.wrapper-position").css("width", con.width);
//                        $("div.wrapper-position").css("height", con.height);
//                        $("svg.designer").css("width", con.width);
//                        $("svg.designer").css("height", con.height);
//                        $("svg.designer").css("background-color", con.bgColor);
//                        $("svg.designer").css("background-image", "url(" + con.bgImage + ")");
//                        $("svg.designer").empty();
//                        $("body").css("background-color", con.bgColor);
//                    }
//                    // json.signals.map(function(signal){
//                    //  if(signal){
//                    // 	 signalMap[''+signal.id]=signal;
//                    //  }
//                    // });
//                    json.elements.map(createElement);
//                }
//            }
//        });
    console.log("开始请求")

    $.ajax({
        url: 'http://tit.suntrans-cloud.com/api/v1/home/floor_plan',
        // url: 'http://192.168.2.234:8080/JsonServlet',
        // data: {'ruleId':$("#ruleId").val(),'dtuSn':$("#dtuSn").val()},
        type: 'POST',
        dataType: "json",
        headers: {
            'Authorization': token
        },
        data: {'house_id': house_id},
        success: function (json) {
            console.log(json)
        }
    });
    // refreshAlarm();
}

//创建元素
function createElement(ele) {
    // if (ele.type == "image") {
        createImage(ele);
    // }
}

function openConfirmDialog(d) {
    console.log(d.href);
     sendCommand(d)

    // var htmlStr = '';
    // htmlStr += '<input type="hidden" id="channel_id" value="0">';
    // htmlStr += '<div class="weui-mask"></div>';
    // htmlStr += '<div class="weui-dialog__hd"><strong class="weui-dialog__title">弹窗标题</strong></div>';
    // htmlStr += '<div class="weui-dialog__bd">';
    // htmlStr += d.title + '</div>';
    // htmlStr += '</div>';
    // htmlStr += '<div class="weui-dialog__ft">';
    // htmlStr += '<a id="qvxiao" href="javascript:void (0);" class="weui-dialog__btn weui-dialog__btn_default">取消</a>';
    // htmlStr += '<a id="queding" href="javascript:void (0);" class="weui-dialog__btn weui-dialog__btn_primary">确定</a>';
    // htmlStr += '</div></div>';
//
//    var tips = d.status ? "是否关闭" : "是否打开";
//
//    $("#title")
//        .text('关闭' + d.title);
//    $("#alertDialog")
//        .show()
//        .data("data", d)
//    $("#queding")
//        .click(function () {
//            sendCommand(d)
//            $("#alertDialog").hide();
//
//        });
//    $("#qvxiao")
//        .click(function () {
//            $("#alertDialog").hide();
//        });
}

function sendCommand(d) {
    console.log('channel_id为：' + d.channel_id);
//    control.switchChannel(d);
    control.switchChannel(d.channel_id+","+d.title+","+d.status);
}

//加载容器属性和元件
// initContainer();
// setInterval("refreshContainer()", 2000);


var tokens;
var house_ids;

function init(token, house_id) {
    tokens = token;
    house_ids = house_id;
    //加载容器属性和元件
     initContainerByToken(tokens,house_ids);
    setInterval("refreshContainerByToken(tokens,house_ids)", 5000);
}


function refreshContainerByToken(token, house_id) {

//    console.log("开始请求")

    $.ajax({
        url: 'http://tit.suntrans-cloud.com/api/v1/home/floor_plan',
        // url: 'http://192.168.2.234:8080/JsonServlet',
        // data: {'ruleId':$("#ruleId").val(),'dtuSn':$("#dtuSn").val()},
        method: 'POST',
        dataType: "json",
        // headers: {
        //     'Authorization': token
        // },

        beforeSend: function (xhr) {
            // token = window.localStorage.getItem('token');
            xhr.setRequestHeader("Authorization", token);
        },

        data: {'house_id': house_id},
        success: function (json) {

            // if (json.code == 200) {
                var con = json.container;
                if (con) {
                    width = con.width;
                    height = con.height;
                    $("div.wrapper-position").css("width", con.width);
                    $("div.wrapper-position").css("height", con.height);
                    $("svg.designer").css("width", con.width);
                    $("svg.designer").css("height", con.height);
                    $("svg.designer").css("background-color", con.bgColor);
                    $("svg.designer").css("background-image", "url(" + con.bgImage + ")");
                    $("svg.designer").empty();
                    $("body").css("background-color", con.bgColor);
                }
                // json.signals.map(function(signal){
                //  if(signal){
                // 	 signalMap[''+signal.id]=signal;
                //  }
                // });
                json.elements.map(createElement);
            // }
        },

    });
    // refreshAlarm();
}

//初始化容器
function initContainerByToken(token, house_id) {
    $.ajax({
        // url: 'http://192.168.2.234:8080/JsonServlet',
        url: 'http://tit.suntrans-cloud.com/api/v1/home/floor_plan',
        // data: {'ruleId': $("#ruleId").val(), 'dtuSn': $("#dtuSn").val()},
        method: 'POST',
        dataType: "json",
        headers: {
            'Authorization': token
        },
        // beforeSend: function (xhr) {
        //     // token = window.localStorage.getItem('token');
        //     xhr.setRequestHeader("Authorization", token);
        // },

        data: {'house_id': house_id},
        success: function (json) {
            //初始化容器样式
            // if (json.result) {
                var con = json.container;
                if (con) {
                    width = con.width;
                    height = con.height;
                    $("div.wrapper-position").css("width", con.width);
                    $("div.wrapper-position").css("height", con.height);
                    $("svg.designer").css("width", con.width);
                    $("svg.designer").css("height", con.height);
                    $("svg.designer").css("background-color", con.bgColor);
                    $("svg.designer").css("background-image", "url(" + con.bgImage + ")");
                    $("svg.designer").empty();
                    $("body").css("background-color", con.bgColor);
                }
                // json.signals.map(function(signal){
                //  if(signal){
                // 	 signalMap[''+signal.id]=signal;
                //  }
                // });
                json.elements.map(createElement);
            }
        // }
    });
    console.log("初始化请求")
}