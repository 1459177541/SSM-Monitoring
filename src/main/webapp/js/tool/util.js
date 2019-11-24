var formatNumber = function (number) {
    var s = number%1000;
    while (number>999){
        n = parseInt(number/1000);
        if (n%1000<10) s='00'+n%1000+','+s;
        else if (n%1000<100) s='0'+n%1000+','+s;
        else s = n%1000+','+s;
        number = n;
    }
    while (s[0]==='0'){
        s = s.substring(1, s.length)
    }
    return s;
};