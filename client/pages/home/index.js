import React, {PropTypes} from 'react';
import Layout from '../../components/Layout/Layout';
import s from './styles.css';

class HomePage extends React.Component {

    static propTypes = {
        articles: PropTypes.array.isRequired
    };

    componentDidMount() {
        document.title = 'Home';
    }

    render() {
        return (
            <Layout className={s.content}>
                <h1 className="content">Home</h1>
                <p>Home page stuff goes here</p>
            </Layout>
        );
    }
}

export default HomePage;