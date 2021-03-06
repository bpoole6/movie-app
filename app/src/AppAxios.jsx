import axios from 'axios'
let baseurl ="/"
const  root = 'api'

if(process.env.REACT_APP_ENV ==='dev'){
     baseurl ="http://localhost:8080/"
}else {
    baseurl = "http://100.26.179.40:8080/"
}

let APP_AXIOS = axios.create({
    baseURL: baseurl,
    headers: {
        'Content-Type': 'application/json'
    }
});

APP_AXIOS.interceptors.response.use((response) => {
    if(response.status === 401) {
        localStorage.removeItem("jwt_token")
        window.location.href  = "/"

    }else {
        console.log(response)
        localStorage.setItem('jwt_token', response.headers['authorization'])
    }
    return response;
}, (error) => {
    if (error.response && error.response.data) {
        return Promise.reject(error.response.data);
    }
    return Promise.reject(error.message);
});
APP_AXIOS.interceptors.request.use(req =>{
    if(!req.headers['authorization'] && localStorage.getItem("jwt_token")){
        req.headers['authorization'] = localStorage.getItem("jwt_token");
    }
    return req;
})
export default APP_AXIOS;


export class ManualApi{

    static login(loginRequest){
       return  APP_AXIOS.post(`${root}/login`,loginRequest);
    }
    static typeAhead(partial){
        return APP_AXIOS.get(`${root}/movie/typeahead/${partial}`)
    }
    static movieDetails(movieId){
        return APP_AXIOS.get(`${root}/movie/${movieId}`);
    }
    static comments(movieId){
        return APP_AXIOS.get(`${root}/comment/movie/${movieId}`)
    }

    static createUpdateComment(comment){
        return APP_AXIOS.put(`${root}/comment/`,comment)
    }
    static deleteComment(commentId){
        return APP_AXIOS.delete(`${root}/comment/${commentId}`)
    }
    static likeComment(commentId,like){
        return APP_AXIOS.put(`${root}/comment/${commentId}/rating`,{'vote':like})
    }
    static movieVote(movieId,rating){
        return APP_AXIOS.put(`${root}/movie/${movieId}/rating`,{'rating':rating})
    }


}
