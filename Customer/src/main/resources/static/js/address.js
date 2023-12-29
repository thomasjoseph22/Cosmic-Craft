const form = document.getElementById('addressForm');
const address_line_1 = document.getElementById('address_line_1');
const address_line_2 = document.getElementById('address_line_2');
const city = document.getElementById('city');
const country = document.getElementById('country');
const pinCode = document.getElementById('pincode');


const setError = (element, message, e) => {
    console.log(element)
    e.preventDefault();
    const inputControl = element.parentElement;
    const errorDisplay = inputControl.querySelector('.error');

    errorDisplay.innerText = message;
    inputControl.classList.add('error');
    inputControl.classList.remove('success');
};
const setSuccess = element => {
    console.log(element)
    const inputControl = element.parentElement;
    const errorDisplay = inputControl.querySelector('.error');
    errorDisplay.innerText = '';
    inputControl.classList.add('success');
    inputControl.classList.remove('error');
};


function validateInputs(e) {
    setSuccess(address_line_1);
    setSuccess(address_line_2);
    setSuccess(city);
    setSuccess(country);
    setSuccess(pinCode);


    const address_line_1_Value = address_line_1.value.trim();
    const address_line_2_Value = address_line_2.value.trim();
    const cityValue = city.value.trim();
    const countryValue = country.value.trim();
    const pinCodeValue = pinCode.value.trim();

    if(address_line_1_Value === '') {
        setError(address_line_1, 'Please enter address line 1', e);
        address_line_1.focus();
        return false;
    }
    else{
        setSuccess(address_line_1);
    }

    if(address_line_2_Value === '') {
        setError(address_line_2, 'Please enter address line 2', e);
        address_line_2.focus();
        return false;
    }
    else{
        setSuccess(address_line_2);
    }

    if(cityValue === '') {
        setError(city, 'Please enter City', e);
        city.focus();
        return false;
    }
    else{
        setSuccess(city);
    }

    if(countryValue === '') {
        setError(country, 'Please enter Country', e);
        country.focus();
        return false;
    }
    else{
        setSuccess(country);
    }



    const pinCodeRegex = /^\d{6}$/;
    if(pinCodeValue === ""){
        setError(pinCode, 'Enter pinCode', e);
        pinCode.focus();
        return false;
    }
    else if(!pinCodeValue.match(pinCodeRegex)){
        setError(pinCode, 'Enter valid pinCode', e);
        pinCode.focus();
        return false;
    }
    else{
        setSuccess(pinCode);
    }


    return true;
}

form.addEventListener('submit', function(e) {
    console.log("hello");
    // e.preventDefault()

    if(validateInputs(e)){
        console.log("VALIDATION Success");
    }
})