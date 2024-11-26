let array = [];
array[''] = new Array({cd:"0", label:"選択してください"});
array["A"] = [
    "SE",
    "ゲーム",
    "サイバー",
    "AI"
];
array["B"] = [
    "初級",
    "中級"
];
array["J"] = [
  "イラスト",
    "3D",
    "ゲーム"
];
array["M"] = [
    "グラフィック",
    "Web"
]
array["F"] = [
    "事務",
    "販売"
];
array["G"] = [
    "医療",
    "調剤",
    "小児"
]
window.addEventListener('load', ()=>{
    document.getElementById('classroom').onchange = function (){
        let course = document.getElementById('course');
        course.options.length = 0;
        let classroomPref = document.getElementById('classroom').value;
        let classroomValue;
        for(let i = 0; i < array[classroomPref].length; i++) {
            let opt = document.createElement('option');
            classroomValue = array[classroomPref][i];
            opt.value = classroomValue;
            opt.text = classroomValue;
            course.appendChild(opt);
        }
    }
})