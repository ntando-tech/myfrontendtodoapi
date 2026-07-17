const forgotPasswordField = document.getElementById("forgotPasswordField");
const forgotPasswordBtn = document.getElementById("forgotPasswordBtn");
const showForgotPasswordMessage = document.getElementById("forgotPasswordMessage");

forgotPasswordBtn.disabled = true;

forgotPasswordField.addEventListener("input", checkForgotPasswordField);

function checkForgotPasswordField() {

    if(forgotPasswordField.value.trim() === "") {
        showForgotPasswordMessage.innerHTML = "";
        forgotPasswordBtn.disabled = true;
    }
    else if(!forgotPasswordField.value.includes("@") || !forgotPasswordField.value.includes(".")){
        forgotPasswordBtn.disabled = true;
    }
    else if(forgotPasswordField.value.includes("@") && forgotPasswordField.value.includes(".")) {

        forgotPasswordBtn.disabled = false;
    }

}
