$(document).ready(function () {
    // Handle tab click event
    $('#address-tab').on('click', function (e) {
        e.preventDefault(); // Prevent default link behavior
        const tabId = $(this).attr('href'); // Get the target tab ID

        // Load address content using AJAX
        $.ajax({
            url: '/customer/address', // Your controller endpoint to fetch address content
            method: 'GET',
            success: function (data) {
                // Update the content of the target tab
                $(tabId + ' .address-content').html(data);
            },
            error: function (error) {
                console.error('Error loading address content:', error);
            }
        });
    });



    $('#orders-tab').on('click', function (e) {
        e.preventDefault(); // Prevent default link behavior
        const tabId2 = $(this).attr('href'); // Get the target tab ID

        // Load address content using AJAX
        $.ajax({
            url: '/customer/orders', // Your controller endpoint to fetch address content
            method: 'GET',
            success: function (data) {
                // Update the content of the target tab
                $(tabId2 + ' .orders').html(data);
            },
            error: function (error) {
                console.error('Error loading address content:', error);
            }
        });
    });

    // $('#wallet-tab').on('click', function (e) {
    //     e.preventDefault(); // Prevent default link behavior
    //     const tabId2 = $(this).attr('href'); // Get the target tab ID
    //
    //     // Load address content using AJAX
    //     $.ajax({
    //         url: '/shop/wallets', // Your controller endpoint to fetch wallet content
    //         method: 'GET',
    //         success: function (data) {
    //             // Update the content of the target tab
    //             $(tabId2 + ' .wallets').html(data);
    //         },
    //         error: function (error) {
    //             console.error('Error loading address content:', error);
    //         }
    //     });
    // });



    $('#account-detail-tab').on('click', function (e) {
        e.preventDefault(); // Prevent default link behavior
        const tabId2 = $(this).attr('href'); // Get the target tab ID

        // Load address content using AJAX
        $.ajax({
            url: '/customer/account-details', // Your controller endpoint to fetch account content
            method: 'GET',
            success: function (data) {
                // Update the content of the target tab
                $(tabId2 + ' .account-details').html(data);
            },
            error: function (error) {
                console.error('Error loading address content:', error);
            }
        });
    });

});




