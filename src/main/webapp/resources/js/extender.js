function chartExtender() {
    var options = {
        plugins: [ChartDataLabels],
        data: {
            datasets: [{
                // Change options only for labels of THIS DATASET
                datalabels: {
                    color: '#16739b',
                    anchor: 'end',
                    align: 'top',
                    rotation: -45,
                    formatter: (val) => {
                        return val + ' mm';
                    }
                },
                minBarLength: 5,
            }, {
                datalabels: {
                    //don't show labels for the second dataset
                    display: false
                }
            }]
        }
    };

    //merge all options into the main chart options
    $.extend(true, this.cfg.config, options);
};