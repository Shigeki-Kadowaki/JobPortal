window.addEventListener('load',()=>{
    const andFlag = document.getElementById('andFlag');
    const andFlagLabel = document.getElementById('andFlagLabel');
    // if(andFlag.checked) andFlagLabel.innerText = "OR";
    andFlag.addEventListener('click', ()=>{
        andFlagLabel.innerText = andFlagLabel.innerText === "AND"?"OR":"AND";
    });
});