import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import {principalId,hasAdminRole} from '../utils/Utils'
import {
    BrowserRouter as Router,
    Switch,
    Route, Link,
    useHistory
} from "react-router-dom";
import NavbarContainer from './NavbarContainer'
import {AsyncTypeahead, Typeahead} from "react-bootstrap-typeahead";
import {ManualApi} from "../AppAxios";
import {withRouter} from "react-router";
import {Button, Card, Form} from "react-bootstrap";

class MovieDetailsContainer extends React.Component {

    constructor() {
        super();
        this.state = {

            movie: undefined,
            comments: [],
            form:{
                comment:{},
                root:{}
            }

        }
        this.editComment = this.editComment.bind(this)
        this.likeComment = this.likeComment.bind(this)
        this.deleteComment = this.deleteComment.bind(this)
        this.onChangeComment = this.onChangeComment.bind(this)
        this.onSubmitComment = this.onSubmitComment.bind(this)
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        if (this.props.match.params.id !== prevProps.match.params.id) {
            this.movieDetails();
            this.comments();
        }
    }

    componentDidMount() {
        this.movieDetails();
        this.comments();
    }

    movieDetails() {

        ManualApi.movieDetails(this.props.match.params.id).then(res => {
            this.setState({movie: res.data});
        }, err =>
            this.props.history.push("/"))
    }

    comments() {

        ManualApi.comments(this.props.match.params.id).then(res => {
            const ids = {}
            for(let parent of res.data){
                for(let child of parent.children){
                    ids[child.id]={}
                }
                ids[parent.parent.id]={}
            }
            const state = Object.assign({}, this.state)
            state.comments = res.data
            state.form.comment = ids;
            state.form.root ={}
            this.setState(state);
        }, err =>
            this.props.history.push("/"))
    }
    editComment(comment){
        return ()=>{

            const state = Object.assign({}, this.state)
            state.form.comment[comment.id].edit = !state.form.comment[comment.id].edit
            state.form.comment[comment.id].value = comment.comment
            this.setState(state);

        }
    }
    likeComment(commentId,didLike){
        return ()=>{
            ManualApi.likeComment(commentId,didLike).then(()=>this.comments(),err=>{
                console.log(err)
            })
        }
    }
    deleteComment(commentId){
        return ()=>{
            ManualApi.deleteComment(commentId).then(res=>{
                this.comments()
            }, err => {

            })
        }
    }
    render() {
        if (!this.state.movie) {
            return null;
        }
        return (
            <React.Fragment>
                <div className={'row justify-content-center pt-3'}>
                    <div className={'col-10'}>
                        <h1>{this.state.movie.title}</h1>
                    </div>
                </div>
                <div className={"row justify-content-center pt-3"}>
                    <div className={"col-6 "}>
                        <img style={{maxWidth: '500px'}}
                             src={this.state.movie.s3Link
                                 ? this.state.movie.s3Link
                                 : "https://movie-app-test-something.s3.amazonaws.com/no_image.jpg"}/>
                    </div>
                    <div className={'col-1 align-self-end'}><h4>Rating:</h4><h5>{(Math.round(this.state.movie.rating * 100) / 100).toFixed(1)}/5 Stars</h5></div>
                    <div className={'col-1 align-self-end'}><h4>Vote:</h4>
                        <select onChange={(ev)=> ManualApi.movieVote(this.props.match.params.id,ev.target.value).then(()=>this.movieDetails())}>
                            <option ></option>
                            <option value={0}>1</option>
                            <option value={1}>2</option>
                            <option value={2}>3</option>
                            <option value={3}>4</option>
                            <option value={4}>5</option>
                        </select>
                    </div>
                </div>
                <div className={'row'}>
                    <div className={'col'}>
                        <h2>Description</h2>
                        <p>{this.state.movie.description}</p>
                    </div>
                </div>
                <div className={'row'}>
                    <div className={'col'}>
                        <h2>Plot</h2>
                        <p>{this.state.movie.plot}</p>
                    </div>
                </div>
                <div className={'row'}>
                    <div className={'col'}>
                        <h3>Cast And Crew</h3>
                        {this.state.movie.crew}
                    </div>
                </div>
                <div className={'row'}>
                    <div className={'col'}>
                        <h3>Release Date</h3>
                        {this.state.movie.premiereDate}
                    </div>
                </div>
                <div className={'row'}>
                    <div className={'col'}><h3>Language</h3>
                        {this.state.movie.language}</div>
                </div>
                <div className={'row'}>
                    <div className={'col'}></div>
                </div>


                <h2>Comments</h2>
                <Form>
                    <Form.Group controlId="comment">
                        <Form.Label>Comment</Form.Label>
                        <Form.Control as="textarea" onChange={this.onChangeComment}  rows={3} />
                    </Form.Group>

                    <Button onSubmit={this.onSubmitComment} onClick={this.onSubmitComment} variant="primary" type="submit">
                        Submit
                    </Button>
                </Form>
                {this.renderComments()}
            </React.Fragment>
        );
    }

    renderComments() {
        const comments = this.state.comments
        const rootComments = [];
        for(let comment of comments){
            const children = [];
            for(let child of comment.children){
                children.push(<React.Fragment >{this.renderComment(child)} </React.Fragment>)
            }
            rootComments.push(<React.Fragment >{this.renderComment(comment.parent)}<div className={"pl-5"}>{children}</div> </React.Fragment>)

        }

        return rootComments;
    }
    onSubmitComment(ev){
        ev.preventDefault();
        const request={
            comment: this.state.form.root.value,
            movieId:this.props.match.params.id
        }
        ManualApi.createUpdateComment(request).then(()=>{
            this.comments();
        })

    }
    onChangeComment(ev){
        const state = Object.assign({}, this.state)
        state.form.root.value = ev.target.value
        this.setState(state);
    }
    onSubmitReply(comment){
        return (ev) =>{
            ev.preventDefault();

            const request={
                comment:this.state.form.comment[comment.id].value,
                movieId:comment.movieId,
                parentId: comment.parentCommentId? comment.parentCommentId : comment.id
            }
              ManualApi.createUpdateComment(request).then(()=>{
                  this.comments();
              })
        }
    }
    onSubmitEdit(comment){
        return (ev) =>{
            ev.preventDefault();

            const request={
                comment:this.state.form.comment[comment.id].value,
                movieId:comment.movieId,
                parentId: comment.parentCommentId,
                commentId:comment.id
            }
            ManualApi.createUpdateComment(request).then(()=>{
                this.comments();
            })
        }
    }
    editCommentOnChange(comment){
        return (ev)=>{
            const state = Object.assign({}, this.state)
            state.form.comment[comment.id].value = ev.target.value
            this.setState(state);
        }
    }
    commentOnChange(comment){
        return (ev)=>{
            const state = Object.assign({}, this.state)
            state.form.comment[comment.id].value = ev.target.value
            this.setState(state);
        }
    }
    commentReply(comment){
        return()=> {
            const state = Object.assign({}, this.state)
            state.form.comment[comment.id].show = !state.form.comment[comment.id].show
            state.form.comment[comment.id].value = ''
            this.setState(state);
        }
    }

    renderComment(comment){
        const actions =[];
        if(principalId() === comment.commentor.id || hasAdminRole()){
            actions.push( <Button onClick={this.editComment(comment)} variant="primary">Edit</Button>)
            actions.push( <Button  onClick={this.deleteComment(comment.id)} variant="primary">delete</Button>)
        }
        return(
            <React.Fragment>
                <Card >
            <Card.Body>
                <Card.Title>{comment.commentor.username} {comment.createdAt}</Card.Title>
                <Card.Text>
                    {comment.comment}
                </Card.Text>
                <Card.Footer>
                    <Button variant="secondary" onClick={this.commentReply(comment)}>Reply</Button>
                    {actions}
                    <Button onClick={this.likeComment(comment.id, true)}  variant={comment.userLike === true ?'primary': 'outline-primary'}>{comment.userLike === true ?'Liked': 'like' } {comment.likes >0? comment.likes : ''}</Button>
                    <Button onClick={this.likeComment(comment.id, false)} variant={comment.userLike === false ?'primary': 'outline-primary'}>{comment.userLike === false?'Disliked': 'Dislike' } {comment.dislikes >0? comment.dislikes : ''}</Button>
                </Card.Footer>
            </Card.Body>
                </Card>
                <Form style={{display: this.state.form.comment[comment.id].show?'block':'none'}}>
                    <Form.Group controlId="comment">
                        <Form.Label>Reply Comment</Form.Label>
                        <Form.Control as="textarea" onChange={this.commentOnChange(comment)}  rows={3} />
                    </Form.Group>

                    <Button onSubmit={this.onSubmitReply(comment)} onClick={this.onSubmitReply(comment)} variant="primary" type="submit">
                        Submit
                    </Button>
                </Form>
                <Form style={{display: this.state.form.comment[comment.id].edit?'block':'none'}}>
                    <Form.Group controlId="comment">
                        <Form.Label>Edit Comment</Form.Label>
                        <Form.Control as="textarea" onChange={this.editCommentOnChange(comment)} value={this.state.form.comment[comment.id].value}  rows={3} ></Form.Control>
                    </Form.Group>

                    <Button onSubmit={this.onSubmitEdit(comment)} onClick={this.onSubmitEdit(comment)} variant="primary" type="submit">
                        Submit
                    </Button>
                </Form>
            </React.Fragment>)
    }
}

export default withRouter(MovieDetailsContainer);
