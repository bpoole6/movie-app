import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
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
import Search from "./Search";
import MovieDetailsContainer from "./MovieDetailsContainer";

class MovieApp extends React.Component {

    constructor() {
        super();
        this.state ={
            options:[],
            selected:[],
            loading: false
        }
        this.movieTypeAhead = this.movieTypeAhead.bind(this);
        this.onMovieSelected = this.onMovieSelected.bind(this);
    }
    movieTypeAhead(query){
        this.setState({loading:true})
        ManualApi.typeAhead(query).then(res=>{
            const options = res.data.map(x => x.title)
            this.setState({options:res.data,loading:false});
        })
    }
    onMovieSelected(opt){
        if(opt.length>0) {
            if(!isNaN(opt[0].id)){
                this.props.history.push("/movie/"+opt[0].id)
            }else {
                this.props.history.push("/search/"+opt[0].title)
            }
        }
    }

    render() {

        return (
            <React.Fragment>

                    <NavbarContainer/>
                    <AsyncTypeahead
                        isLoading={this.state.loading}
                        allowNew
                        id="basic-typeahead-multiple"
                        labelKey='title'
                        onSearch={(query)=>this.movieTypeAhead(query)}
                        options={this.state.options}
                        placeholder="What movie are you looking for"
                        onChange={this.onMovieSelected}
                        // selected={this.state.selected}
                    />
                    <React.Fragment>
                        <Switch>
                            <Route exact path="/">
                                <h1>Home</h1>
                            </Route>
                            <Route path="/search">
                                fudge
                            </Route>
                            <Route path="/movie/:id">
                            <MovieDetailsContainer/>
                            </Route>

                        </Switch>
                    </React.Fragment>
            </React.Fragment>
        );
    }
}
export default withRouter(MovieApp);
