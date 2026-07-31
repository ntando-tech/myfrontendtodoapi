const currentPassword = document.getElementById("currentPassword");
const newPassword = document.getElementById("newPassword");
const confirmNewPassword = document.getElementById("confirmNewPassword");
const changePasswordBtn = document.getElementById("changePasswordBtn");
const changePasswordMessage = document.getElementById("showNewPasswordMessage");


changePasswordBtn.disabled = true;


newPassword.addEventListener("input", checkChangePasswords);
confirmNewPassword.addEventListener("input", checkChangePasswords);
function checkChangePasswords() {

    if(newPassword.value.trim() === "" && confirmNewPassword.value.trim() === "" && currentPassword.value.trim() === "") {
        changePasswordMessage.innerHTML = "";
        changePasswordBtn.disabled = true;
    }
    else if(newPassword.value.trim().length > 0 && newPassword.value.trim().length < 8 || confirmNewPassword.value.trim().length > 0 && confirmNewPassword.value.trim().length < 8){
        changePasswordMessage.style.color = "red";
        changePasswordMessage.innerHTML = "Password must have atleast 8 characters";
        changePasswordBtn.disable = true;
    }
    else if(newPassword.value.trim() === confirmNewPassword.value.trim()) {
        changePasswordMessage.style.color = "green";
        changePasswordMessage.innerHTML = "Both passwords are matching";
        changePasswordBtn.disabled = false;

    }
    else {
        changePasswordMessage.style.color = "red";
        changePasswordMessage.innerHTML = "Both passwords must match";
        changePasswordBtn.disabled = true;
    }
}

function showCurrentPassword(){
    let input = document.getElementById("currentPassword");
    let icon = document.getElementById("currentPasswordEyeIcon");

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

function showNewPassword(){
    let input = document.getElementById("newPassword");
    let icon = document.getElementById("newPasswordEyeIcon");

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

function showConfirmNewPassword(){
    let input = document.getElementById("confirmNewPassword");
    let icon = document.getElementById("confirmNewPasswordEyeIcon");

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
