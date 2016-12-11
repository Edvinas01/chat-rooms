import React, {Component, PropTypes} from 'react';
import ReactDOM from 'react-dom';
import $ from 'jquery';
import './createModal.css'

class CreateModal extends Component {

    constructor(props) {
        super(props);

        this.state = {
            name: ''
        };

        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleChange = this.handleChange.bind(this);
    }

    componentDidMount() {
        $(ReactDOM.findDOMNode(this)).modal('show');
        $(ReactDOM.findDOMNode(this)).on('hidden.bs.modal', this.props.handleHide);
    }

    handleSubmit(e) {
        e.preventDefault();
        this.props.handleSubmit(this.state.name);
        $(ReactDOM.findDOMNode(this)).modal('hide');
    }

    handleChange(e) {
        this.setState({
            name: e.target.value
        });
    }

    render() {
        const {name} = this.state;

        return (
            <div className="modal fade">
                <div className="modal-dialog">
                    <div className="modal-content">
                        <div className="modal-header">
                            <button type="button"
                                    className="close"
                                    data-dismiss="modal"
                                    aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                            <h4 className="modal-title">Create channel</h4>
                        </div>
                        <form onSubmit={this.handleSubmit}>
                            <div className="modal-body">

                                <div className="form-group">
                                    <label>Name</label>
                                    <input className="form-control"
                                           onChange={this.handleChange}
                                           value={name}
                                           required
                                           placeholder="Channel name..."
                                           name="name"/>
                                </div>
                            </div>
                            <div className="modal-footer">
                                <button type="submit"
                                        className="btn btn-success btn-space">
                                    Create
                                </button>
                                <button type="button"
                                        className="btn btn-danger"
                                        data-dismiss="modal">
                                    Close
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        );
    }
}

CreateModal.propTypes = {
    handleSubmit: PropTypes.func.isRequired,
    handleHide: PropTypes.func.isRequired
};

export default CreateModal;