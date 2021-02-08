import React from 'react';
import {Button, Form} from "react-bootstrap";
import {ManualApi} from "../AppAxios";
class Login extends React.Component {
    constructor(props) {
        super();
        this.state = {
            form:{username:'',password:''}
        }
        this.handleChange = this.handleChange.bind(this)
        this.onSubmit = this.onSubmit.bind(this)
    }

    handleChange(event){
        let form = Object.assign({},this.state.form)
        form[event.target.getAttribute('name')]=event.target.value;
        console.log(form)
        this.setState({form:form});
    }
    onSubmit(ev){
        ev.preventDefault();
        console.log("lol")
        ManualApi.login(this.state.form).then((res)=>{
            this.props.resetState();
        },(err)=>{
            console.log(err)
        })

    }
    render() {

        return (
            <Form>
                <Form.Group controlId="username">
                    <Form.Label>Username</Form.Label>
                    <Form.Control  value={this.state.form.username} name={'username'} onChange={this.handleChange} type="text" placeholder="Enter Username" />
                </Form.Group>

                <Form.Group controlId="password">
                    <Form.Label>Password</Form.Label>
                    <Form.Control value={this.state.form.password} name={'password'} onChange={this.handleChange} type="password" placeholder="Password" />
                </Form.Group>
                <Form.Group controlId="formBasicCheckbox">
                    <Form.Check type="checkbox" label="Check me out" />
                </Form.Group>
                <Button onSubmit={this.onSubmit} onClick={this.onSubmit} variant="primary" type="submit">
                    Submit
                </Button>
            </Form>
        )
    }
}

export default Login;
