import React from 'react'
import Moment from 'moment';
import { Link } from "react-router-dom"
class ListPage extends React.Component<any, any>{

    constructor(props: any) {
        super(props);
        this.state = { seckillList: [] }
        const GET_SECKILL_LIST = 'http://localhost:8080/seckill/seckill/list';
        fetch(GET_SECKILL_LIST)
            .then(response => response.json())
            .then(jsonResponse => {
                this.setState({ seckillList: jsonResponse });
            });
    }

    render() {
        Moment.locale('en');
        return (
            <div>
                <table>
                    <thead>
                        <tr>
                            <th>Name</th>
                            <th>Stock</th>
                            <th>Start Time</th>
                            <th>End Time</th>
                            <th>Create Time</th>
                            <th>Detail Page</th>
                        </tr>
                    </thead>
                    <tbody>
                        {this.state.seckillList ? this.state.seckillList.map((item: any, i: any) => (
                            <tr key={i}>
                                <th >{item.name}</th>
                                <th>{item.number}</th>
                                <th>{Moment(new Date(item.startTime)).format('yyyy-MM-DD HH:MM:ss')}</th>
                                <th>{Moment(new Date(item.endTime)).format('yyyy-MM-DD HH:MM:ss')}</th>
                                <th>{Moment(new Date(item.createTime)).format('yyyy-MM-DD HH:MM:ss')}</th>
                                <th><Link className="btn btn-info" to={{
                                    pathname: '/seckill/' + item.seckillId + '/detail',
                                    state: {
                                        seckillId: item.seckillId,
                                        startTime: item.startTime,
                                        endTime: item.endTime
                                    }
                                }} > Details</Link></th>
                            </tr>
                        )) : null}
                    </tbody>
                </table>
            </div>
        )
    }


}

export default ListPage;
