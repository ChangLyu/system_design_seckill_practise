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
            currentTime: 0,
            seckillState: SeckillStates.LOADING,
            timer: null,
            remaingingMilleSec: 0,
            timeRemain: 0,
            seckillExposer: null
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
                const countDownTime = this.state.value.startTime - this.state.currentTime;
                this.setState({
                    remaingingMilleSec: countDownTime,
                    timer: setInterval(this.countDown.bind(this), 1000)
                });
            } else {
                // start
                this.setState({ seckillState: SeckillStates.START })
                this.getSeckillExposer();
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

    getSeckillExposer() {
        const GET_SECKILL_EXPOSER = `http://localhost:8080/seckill/seckill/${this.state.value.seckillId}/exposer`;
        fetch(GET_SECKILL_EXPOSER)
            .then(response => response.json())
            .then(jsonResponse => {
                this.setState({ seckillExposer: jsonResponse.data });
            });
    }

    displaySeckill() {
        return (
            this.state.seckillExposer ?
                <div>
                    {this.state.seckillExposer.seckillId}
                </div> : <div>LOADING...</div>
        );
    }

    renderSeckillInfor() {
        switch (this.state.seckillState) {
            case SeckillStates.NOT_START:
                return (<div><div>Kill has not started yet!</div>
                    <div>{this.state.timeRemain.days} Days, {this.state.timeRemain.hours} Hours, {this.state.timeRemain.minutes}Minutes, {this.state.timeRemain.seconds} Seconds</div>
                </div>);
            case SeckillStates.START:
                return (
                    <div>
                        <div>Kill start!</div>
                        {this.displaySeckill()}
                    </div>);
            case SeckillStates.ENDED:
                return (<div> This Seckill {this.state.value.seckillId} is already closed! </div>);
            default:
                return (<div> LOADING....</div>);
        }
    }

    countDown() {
        if (this.state.remaingingMilleSec && this.state.remaingingMilleSec > 1000) {
            let timeRemain = this.state.remaingingMilleSec - 1000;
            this.setState({ remaingingMilleSec: timeRemain });
            let remainTime = this.getRemainingTime();
            this.setState({ timeRemain: remainTime });
        } else {
            this.setState({ seckillState: SeckillStates.START });
            clearInterval(this.state.timer);

        }
    }


    getRemainingTime() {
        const difference = this.state.remaingingMilleSec;
        const seconds = Math.floor((difference / 1000) % 60);
        const minutes = Math.floor((difference / 1000 / 60) % 60);
        const hours = Math.floor((difference / 1000 * 60 * 60) % 24);
        const days = Math.floor(difference / (1000 * 60 * 60 * 24));
        return {
            days, hours, minutes, seconds
        };
    }


    render() {
        console.log('render');
        return (
            this.state.register ?
                <div>
                    register your phone pls:
                    <input type="text" onChange={this.updateInput.bind(this)}></input>
                    <button onClick={this.registerPhone.bind(this)}>Register</button>
                </div> : (
                    this.state.value ?
                        (
                            this.state.seckillState === SeckillStates.LOADING ?
                                (<div>LOADING....</div>) : this.renderSeckillInfor()
                        ) : <div>Invalid URL, please check detail from List</div>
                )


        )
    }

}
