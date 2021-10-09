import React from 'react'
import Cookies from 'js-cookie';
import { SeckillStates } from '../../constants/constants';
import { Case, Switch } from '../../utils/Switch';
import { Link } from 'react-router-dom';
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
            seckillExposer: null,
            seckillExposerURL: null,
            killResult: null
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
        if (this.state.value) {
            if (this.state.value.endTime < this.state.currentTime) {
                // ended
                this.setState({ seckillState: SeckillStates.ENDED })
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
                if (!this.state.seckillExposer.exposed) {
                    this.setState({
                        currentTime: jsonResponse.data.now,
                        value: {
                            ...this.state.value,
                            startTime: jsonResponse.data.start
                        }
                    });
                    this.updateSeckillState();
                } else {
                    this.setState({ seckillExposerURL: `http://localhost:8080/seckill/seckill/${this.state.seckillExposer.seckillId}/${this.state.seckillExposer.md5}/execution` });
                }
            });
    }
    beginKill() {
        const requestOptions = {
            method: 'POST',
            credentials: 'include',
            headers: { 'Content-Type': 'application/json' }
        }
        fetch(this.state.seckillExposerURL, requestOptions as any)
            .then(response => response.json())
            .then(jsonResponse => {
                this.setState({ killResult: jsonResponse.data ? jsonResponse.data.stateInfo : null })
            });
    }

    displaySeckill() {
        return (
            <div>
                {
                    this.state.seckillExposer && this.state.seckillExposer.exposed ?
                        <div>

                            {this.state.seckillExposerURL}
                            <button onClick={this.beginKill.bind(this)}> KILL!</button>

                        </div> : <div>LOADING...</div>
                }
                {this.state.killResult && <div> {this.state.killResult}</div>}
            </div>
        );
    }

    renderSeckillInfor() {
        return (
            <Switch value={this.state.seckillState}>
                <Case value={SeckillStates.NOT_START}>
                    <div><div>Kill has not started yet!</div>
                        <div>{this.state.timeRemain.days} Days, {this.state.timeRemain.hours} Hours, {this.state.timeRemain.minutes}Minutes, {this.state.timeRemain.seconds} Seconds</div>
                    </div>
                </Case>
                <Case value={SeckillStates.START}>
                    <div>
                        <div>Kill start!</div>
                        {this.displaySeckill()}
                    </div>
                </Case>
                <Case value={SeckillStates.ENDED}>
                    <div> This Seckill {this.state.value.seckillId} is already closed! </div>
                </Case>
                <Case value={SeckillStates.LOADING}>
                    <div> LOADING SECKILL INFORMATION....</div>
                </Case>
            </Switch>
        )
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
