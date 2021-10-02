import './App.css';
import DetailPage from './seckill/detail/DetailPage';
import ListPage from './seckill/list/ListPage';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import '../node_modules/bootstrap/dist/css/bootstrap.min.css';
function App() {
  return (
    <div className="App">
      <h1>Seckill products</h1>
      <Router>
        <Switch>
          <Route exact path="/" component={ListPage} />
          <Route path={`/seckill/:seckillId/detail`} component={DetailPage} />
        </Switch>
      </Router>
    </div>
  );
}

export default App;
