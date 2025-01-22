
function toggleANDText(){
    const andFlagLabel = document.getElementById('andFlagLabel')
    andFlagLabel.innerText = andFlagLabel.innerText === "AND"?"OR":"AND";
}
//学生データ(jsバージョン)
// window.addEventListener('load',()=>{
//     fetch('http://172.16.0.3/api/students/40104',{
//         method: 'GET',
//         mode: "cors"
//     })
//         .then(response => response.json())
//         .then(json =>{
//             console.log(json)
//             json.forEach(e=>{
//                 console.log(typeof e.class);
//             })
//             console.log("test");
//         })
//         .catch(error => console.error('Error:', error))
// })
