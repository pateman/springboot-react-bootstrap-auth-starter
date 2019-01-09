import React from 'react';
import {Redirect} from 'react-router-dom';

const dashboardPage = (props) => {
    if (!props.user) {
        return <Redirect to='/'/>;
    }

    const specializedParagraph = props.user.role[0] === 'ROLE_ADMIN' ?
        <p>This text is only visible for admins</p> :
        <p>This message is visible by any user</p>;

    return (
        <div className="row">
            <div className="col">
                <h1>Hello, {props.user.name}!</h1>
                {specializedParagraph}
            </div>
        </div>
    );
};

export default dashboardPage;

