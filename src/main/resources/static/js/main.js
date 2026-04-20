function showPassword(){
    let input = document.getElementById("signupPassword");
    let icon = document.getElementById("passwordEyeIcon");

    if("password" === (input.type).trim())
    {
        input.type = "text";
        icon.classList.remove("bi-eye-fill");
        icon.classList.add("bi-eye-slash-fill");
    }
    else if("text" === (input.type).trim()){
        input.type = "password";
        icon.classList.remove("bi-eye-slash-fill");
        icon.classList.add("bi-eye-fill");
    }
}

function showConfirmPassword(){
    let input = document.getElementById("signupConfirmPassword");
    let icon = document.getElementById("confirmPasswordEyeIcon");

    if("password" === (input.type).trim())
    {
        input.type = "text";
        icon.classList.remove("bi-eye-fill");
        icon.classList.add("bi-eye-slash-fill");
    }
    else if("text" === (input.type).trim()){
        input.type = "password";
        icon.classList.remove("bi-eye-slash-fill");
        icon.classList.add("bi-eye-fill");
    }
}

function showSigninPassword(){
    let input = document.getElementById("signinPassword");
    let icon = document.getElementById("signinPasswordEyeIcon");

    if("password" === (input.type).trim())
    {
        input.type = "text";
        icon.classList.remove("bi-eye-fill");
        icon.classList.add("bi-eye-slash-fill");
    }
    else if("text" === (input.type).trim()){
        input.type = "password";
        icon.classList.remove("bi-eye-slash-fill");
        icon.classList.add("bi-eye-fill");
    }
}