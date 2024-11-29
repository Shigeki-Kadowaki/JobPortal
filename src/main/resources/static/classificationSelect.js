let array = [];
array[''] = new Array({cd:"0", label:"選択してください"});
array["A"] = [[
    "A1",
    "AG"
],[
    "SE",
    "ゲーム",
    "サイバー",
    "AI"
]];
array["B"] = [[
    "B1",
    "BG"
],[
    "初級",
    "中級"
]];
array["J"] = [[
    "イラスト",
    "3D",
    "ゲーム"
],[
    "イラスト",
    "3D",
    "ゲーム"
]];
array["M"] = [[
    "グラフィック",
    "Web"
],[
    "グラフィック",
    "Web"
]];
array["F"] = [[
    "F"
],[
    "事務",
    "販売"
]];
array["G"] = [[
    "G"
],[
    "医療",
    "調剤",
    "小児"
]];
let grade;
function toggleSelect(){
    if(document.getElementById('grade').value !== ""){
        grade = document.getElementById('grade').value - 1;
    }else{
        grade = 0;
    }
    let course = document.getElementById('course');
    course.options.length = 0;
    let classroomPref = document.getElementById('classroom').value;
    if(classroomPref === "") return;
    let classroomValue;
    for(let i = 0; i < array[classroomPref][grade].length; i++) {
        let opt = document.createElement('option');
        classroomValue = array[classroomPref][grade][i];
        opt.value = classroomValue;
        opt.text = classroomValue;
        if(selectedCourse === classroomValue) opt.selected = true;
        course.appendChild(opt);
    }

}
window.addEventListener('load', ()=>{
    document.getElementById('grade').addEventListener('change',toggleSelect);
    document.getElementById('classroom').addEventListener('change',toggleSelect);
    toggleSelect();
})