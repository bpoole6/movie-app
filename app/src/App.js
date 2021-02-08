import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import {
  BrowserRouter as Router,
  Switch,
  Route,
  Link,
  useHistory
} from "react-router-dom";
import {Button, Modal, Nav, Navbar} from "react-bootstrap";
import Login from "./components/Login";
import {withRouter} from "react-router";
import MovieApp from "./components/MovieApp";
import {isAuthenticated} from "./utils/Utils.js"
import AppAxios from "./AppAxios";
class App extends React.Component {

  constructor() {
    super();
    this.resetView = this.resetView.bind(this);
  }
  resetView(){
    this.setState({});
  }
  render() {

    const view = isAuthenticated() ? <MovieApp/> : <Login resetState ={()=>this.setState({})}/>;
    return (
        <div className="container">
          <Router>
          {view}
          </Router>
        </div>
    );
  }
}

export default App;
