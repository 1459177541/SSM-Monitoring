var disk = function () {
    $.get(
        'console/disk',
        function (data) {
            data.data.forEach(function (element) {
                var rate = (element.use/element.size)*100;
                $('#disk_main').append(
                    "<div class=\"disk\" style=\'" +
                    "background-image: linear-gradient(to right, " +
                                            "aquamarine "+rate+"%," +
                                            "rgba(0,0,0,0) "+rate+"%)\'>" +
                        "磁盘名："+element.name+"<br/>" +
                        "路径："+element.url+"<br/>" +
                        "总容量："+formatNumber(element.size)+"KB<br/>" +
                        "已使用量:"+formatNumber(element.use)+"KB<br/>" +
                    "</div>"
                );
            });
        }
    )
};