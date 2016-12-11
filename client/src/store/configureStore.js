import {createStore, applyMiddleware, combineReducers, compose} from 'redux';
import thunkMiddleware from 'redux-thunk';
import createLogger from 'redux-logger';

import register from '../reducers/register';
import account from '../reducers/account';
import channels from '../reducers/channels';
import channel from '../reducers/channel';
import users from '../reducers/users';
import login from '../reducers/login';

const logger = createLogger();
const rootReducer = combineReducers({
        register,
        account,
        channels,
        channel,
        users,
        login
    }
);

const initialState = {};

export default function configureStore() {
    let store;

    if (module.hot) {
        store = createStore(rootReducer, initialState, compose(
            applyMiddleware(thunkMiddleware, logger),
            window.devToolsExtension ? window.devToolsExtension() : f => f
        ));
    } else {
        store = createStore(rootReducer, initialState, compose(
            applyMiddleware(thunkMiddleware), f => f
        ));
    }
    return store;
}