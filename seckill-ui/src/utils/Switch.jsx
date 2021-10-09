
export const Switch = props => {
  const { value, children } = props
  // filter out only children with a matching prop
  return children.find(child => {
    return child.props.value === value
  })
}

export const Case = ({ children, value }) => { return children; };
