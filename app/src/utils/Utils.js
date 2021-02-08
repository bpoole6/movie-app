
const parseJwt = () => {
    const token = localStorage.getItem("jwt_token")
    if(token && token!=='undefined') {
        var base64Payload = token.split('.')[1];
        var payload = atob(base64Payload);
        return JSON.parse(payload.toString());
    }
    else return {};
}

const isAuthenticated = () => {
    const token = parseJwt();
    if(Object.keys(token).length>0 && token.exp<=new Date().getTime()){
        return true;
    }else {
        return false;
    }
}
const principalId = () =>{
    const token = parseJwt();
    if(Object.keys(token).length>0){
        return token.id
    }
    return null;
}
const hasAdminRole =() =>{
    const token = parseJwt();
    return !!token.roles.map(x => x.toLowerCase()).includes("admin");
}


export {parseJwt, isAuthenticated, hasAdminRole, principalId};
