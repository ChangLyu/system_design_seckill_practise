import React from 'react'
import Cookies from 'js-cookie';
import { SeckillStates } from '../../constants/constants';
export default class DetailPage extends React.Component<any, any> {

    constructor(props: any) {
        super(props);
        const phone = Cookies.get('userPhone');
        this.state = {
            value: props.location.state,
            userPhone: phone,
            register: !this.validatePhone(phone),
            currentTime: new Date().getTime,
            seckillState: SeckillStates.NOT_START
        };

    }
    componentDidMount() {
        const GET_CURRENT_TIME = 'http://localhost:8080/seckill/seckill/time/now';
        fetch(GET_CURRENT_TIME)
            .then(response => response.json())
            .then(jsonResponse => {
                this.setState({ currentTime: jsonResponse.data });
                this.updateSeckillState();
            });
    }

    validatePhone(phoneNumber: any) {
        return phoneNumber && phoneNumber.length === 11 && !isNaN(parseInt(phoneNumber));
    }
    updateSeckillState() {
        console.log("coundone");
        if (this.state.value) {
            if (this.state.value.endTime < this.state.currentTime) {
                // ended
                console.log('set state before : ' + this.state.seckillState)
                this.setState({ seckillState: SeckillStates.ENDED })
                console.log('set state after ' + this.state.seckillState)
            } else if (this.state.value.startTime > this.state.currentTime) {
                // not begin yet
                this.setState({ seckillState: SeckillStates.NOT_START })
            } else {
                // start
                this.setState({ seckillState: SeckillStates.START })
            }
        }


    }
    updateInput(event: any) {
        this.setState({ userPhone: event.target.value })
    }
    registerPhone(event: any) {

        if (this.validatePhone(this.state.userPhone)) {
            Cookies.set('userPhone', this.state.userPhone);
            window.location.reload();
        }
    }

    renderSeckillInfor() {
        console.log(this.state.seckillState);
        switch (this.state.seckillState) {
            case SeckillStates.NOT_START:
                return (<div>
                    <div>Kill has not started yet!</div>
                    <div>{this.state.value.seckillId}</div>
                    <div>{this.state.value.startTime}</div>
                    <div>{this.state.value.endTime}</div>
                </div>);
            case SeckillStates.START:
                return (<div>Kill start!</div>);
            default:
                return (<div> This Seckill {this.state.value.seckillId} is already closed! </div>);
        }
    }


    render() {
        console.log('render');
        return (
            this.state.register ?
                <div>
                    register your phone pls:
                    <input type="text" onChange={this.updateInput.bind(this)}></input>
                    <button onClick={this.registerPhone.bind(this)}>Register</button>
                </div> :
                this.state.value ?
                    this.renderSeckillInfor() : <div>Invalid URL, please check detail from List</div>

        )
    }

}
