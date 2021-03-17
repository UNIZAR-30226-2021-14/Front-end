import React from 'react'
import PropTypes from 'prop-types'
//Tipo clase pues manejo el estado
class Search extends React.Component {

  constructor(props){
    super(props)
    this.state = {search: ''}
  }

  handleChange = (e) => {
    this.setState({search: e.target.value})
    //console.log(e.target.value) //target es quien desencadenó el evento. Input en nuestro caso, que tiene value
  }

  render () {
    const {handleSearch} = this.props
    const {search} = this.state
    return (
      <div className="search-container">
        <input 
          value = {this.state.search}
          className="search-input" 
          type="text"
          onChange={this.handleChange}
          /> 
        <button className="search-btn" onClick = {() => handleSearch(search)}>Search</button>
      </div>
    )
  }
}
Search.propTypes = {
  handleSearch:PropTypes.func.isRequired
}
export default Search