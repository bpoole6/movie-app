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
import {Button, Card} from "react-bootstrap";

class MovieDetailsContainer extends React.Component {

    constructor() {
        super();
        this.state = {

            movie: undefined,
            comments: []
        }
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
            this.setState({comments: res.data});
        }, err =>
            this.props.history.push("/"))
    }

    render() {
        if (!this.state.movie) {
            return null;
        }
        console.log(this.state.movie)
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
                    <div className={'col-1 align-self-end'}><h4>Rating:</h4><h5>{this.state.movie.rating}/10 Stars</h5></div>
                </div>
                <div className={'row '}>
                    <div className={'col '}>
                        <h2>Vote</h2>
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
                {/*{this.renderComments()}*/}
            </React.Fragment>
        );
    }

    renderComments() {
        const comments = this.state.comments

        return (<div>
            <div>date</div>
            <p>comment</p>
            <div>reply</div>
            <div>edit</div>
            <div>delete</div>
        </div>)
    }
    editComment(commentId){
        return ()=>{

        }
    }
    likeComment(commentId,didLike){
        return ()=>{
            ManualApi.likeComment(commentId,didLike)
        }
    }
    deleteComment(commentId){
        return ()=>{
            ManualApi.deleteComment(commentId).then(res=>{

            }, err => {
                this.comments()
            })
        }
    }

    renderComment(comment){
        const actions =[];
        if(principalId() === comment.commentor.id || hasAdminRole()){
            actions.push( <Button onClick={this.editComment(comment.id)} variant="primary">Edit</Button>)
            actions.push( <Button onClick={this.deleteComment(comment.id)} variant="primary">delete</Button>)
        }
        return(<Card style={{ width: '18rem' }}>
            <Card.Body>
                <Card.Title>{comment.commentor.username} {comment.commentor.createdAt}</Card.Title>
                <Card.Text>
                    Some quick example text to build on the card title and make up the bulk of
                    the card's content.
                </Card.Text>

                <Card.Footer>
                    <Button variant="primary">Edit</Button>
                    <Button variant="primary">delete</Button>
                    <Button onClick={this.likeComment(comment.id, true)} variant="primary">>{comment.userLike === true ?'Liked': 'like' } {comment.likes >0? comment.likes : ''}</Button>
                    <Button  onClick={this.likeComment(comment.id, false)} variant="primary">{comment.userLike === false?'Disliked': 'Dislike' } {comment.dislikes >0? comment.dislikes : ''}</Button>
                </Card.Footer>
            </Card.Body>
        </Card>)
    }
}

export default withRouter(MovieDetailsContainer);
