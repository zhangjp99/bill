$(document).ready(function(){
	var $province = $('#province');
    var $city= $('#city');
    var $region= $('#region');

    if(!EOVA_FORM)
		return;
    
    var cmbProvince = $province.eovacombo();
    var cmbCity = $city.eovacombo();
    var cmbRegion = $region.eovacombo();

    // 初始禁用
    cmbCity.readonly(true);
    cmbRegion.readonly(true);

 	// 省级联市
    $province.eovacombo({onChange: function (oldValue, newValue) {
        cmbCity.setValue("");
        cmbRegion.setValue("");

        if (newValue == "") {
        	cmbCity.readonly(true);
            return;
        }
        cmbCity.readonly(false);
        
        $city.eovacombo({exp : 'selectAreaByLv2AndPid,' + newValue}).reload();
    }});
    // 市级联区县
    $city.eovacombo({onChange: function (oldValue, newValue) {
        cmbRegion.setValue("");

        if (newValue == "") {
        	cmbRegion.readonly(true);
            return;
        }
        cmbRegion.readonly(false);
        $region.eovacombo({exp : 'selectAreaByLv3AndPid,' + newValue}).reload();

    }});
});