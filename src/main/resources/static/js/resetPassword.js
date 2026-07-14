const firstResetPassword = document.getElementById("resetPassword");
const secondResetPassword = document.getElementById("confirmResetPassword");
const resetPasswordMessage = document.getElementById("resetPasswordMessage");
const resetPasswordBtn = document.getElementById("resetPasswordBtn");

resetPasswordBtn.disabled = true;

secondResetPassword.addEventListener("input", checkResetPasswords);
firstResetPassword.addEventListener("input", checkResetPasswords);
function checkResetPasswords() {

    if(firstResetPassword.value.trim() === "" && secondResetPassword.value.trim() === "") {
        resetPasswordMessage.innerHTML = "";
        resetPasswordBtn.disabled = true;
    }
    else if(firstResetPassword.value.trim().length > 0 && firstResetPassword.value.trim().length < 8 || secondResetPassword.value.trim().length > 0 && secondResetPassword.value.trim().length < 8){
        resetPasswordMessage.style.color = "red";
        resetPasswordMessage.innerHTML = "Password must have atleast 8 characters";
        resetPasswordBtn.disable = true;
    }
    else if(firstResetPassword.value.trim() === secondResetPassword.value.trim()) {
        resetPasswordMessage.style.color = "green";
        resetPasswordMessage.innerHTML = "Both passwords are matching";
        resetPasswordBtn.disabled = false;

    }
    else {
        resetPasswordMessage.style.color = "red";
        resetPasswordMessage.innerHTML = "Both passwords must match";
        resetPasswordBtn.disabled = true;
    }
}


function showResetPassword(){
    let input = document.getElementById("resetPassword");
    let icon = document.getElementById("resetPasswordEyeIcon");

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

function showConfirmResetPassword(){
    let input = document.getElementById("confirmResetPassword");
    let icon = document.getElementById("confirmResetPasswordEyeIcon");

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
