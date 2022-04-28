function acquireAuthentication(profileLinkID, optionsLinkID, loginLinkID,
                               logoutLinkID, signUpLinkID ){
    let request = new XMLHttpRequest();
    request.open('GET', '/security/user');
    request.onload = function(){
        const username = JSON.parse(request.responseText).username;
        const id = JSON.parse(request.responseText).id;
        const autherities = JSON.parse(request.responseText).authorities;
        if(request.status === 200 && username.search('anonymous') === -1){
            document.getElementById(profileLinkID).style.display = 'block';
            document.getElementById(optionsLinkID).style.display = 'block';
            document.getElementById(logoutLinkID).style.display = 'block';
            document.getElementById(signUpLinkID).style.display = 'none';
            document.getElementById(loginLinkID).style.display = 'none';

        }else{
            document.getElementById(profileLinkID).style.display = 'none';
            document.getElementById(optionsLinkID).style.display = 'none';
            document.getElementById(logoutLinkID).style.display = 'none';
            document.getElementById(signUpLinkID).style.display = 'block';
            document.getElementById(loginLinkID).style.display = 'block';
        }
    }
    request.send();
}

