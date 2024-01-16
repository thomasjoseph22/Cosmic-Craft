(function ($) {
    "use strict";

    /*Sale statistics Chart*/
    if ($('#myChart').length) {
        console.log("chart called");
        var salesData = [];
        $.get('/admin/chart',function (data){
            console.log(data);
            const salesData=data.salesData;
            const revenueData=data.revenueData;

            const ctx = document.getElementById('myChart').getContext('2d');
            const chart = new Chart(ctx, {
                // The type of chart we want to create
                type: 'line',

                // The data for our dataset
                data: {
                    labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
                    datasets: [{
                        label: 'No. Of Orders In Each Month',
                        tension: 0.3,
                        fill: true,
                        backgroundColor: 'rgba(44, 120, 220, 0.2)',
                        borderColor: 'rgba(44, 120, 220)',
                        data: salesData
                    },
                        {
                            label: 'Total Revenue In Each Month',
                            tension: 0.3,
                            fill: true,
                            backgroundColor: 'rgba(100, 209, 130, 0.2)',
                            borderColor: 'rgb(100, 209, 130)',
                            data: revenueData
                        },
                    ]
                },
                options: {
                    plugins: {
                        legend: {
                            labels: {
                                usePointStyle: true,
                            },
                        }
                    }
                }
            })
        });
    }//End if

    /*Sale statistics Chart*/
    if ($('#myChart2').length) {
        const ctx = document.getElementById("myChart2");
        const myChart = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: ["900", "1200", "1400", "1600"],
                datasets: [
                    {
                        label: "US",
                        backgroundColor: "#5897fb",
                        barThickness:10,
                        data: [233,321,783,900]
                    },
                    {
                        label: "Europe",
                        backgroundColor: "#7bcf86",
                        barThickness:10,
                        data: [408,547,675,734]
                    },
                    {
                        label: "Asian",
                        backgroundColor: "#ff9076",
                        barThickness:10,
                        data: [208,447,575,634]
                    },
                    {
                        label: "Africa",
                        backgroundColor: "#d595e5",
                        barThickness:10,
                        data: [123,345,122,302]
                    },
                ]
            },
            options: {
                plugins: {
                    legend: {
                        labels: {
                            usePointStyle: true,
                        },
                    }
                },
                scales: {
                    y: {
                        beginAtZero: true
                    }
                }
            }
        });
    } //end if

})(jQuery);