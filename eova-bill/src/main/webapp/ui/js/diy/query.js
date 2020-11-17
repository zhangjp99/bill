// 查询前置检查
function eovaQueryBefore() {
    let start = $("#start_v_day").val();
    let end = $("#end_v_day").val();

    if (start == '' || end == ''){
        $.msg("请先选择入账日期，不超过60天");
        return;
    }

    let a = new Date(start);
    let b = new Date(end);
    if ((b - a) / (1000 * 60 * 60 * 24) > 60) {
        $.msg("最大查询时间为60天");
        return false;
    }
    return true;
}