let array = [];
array[''] = new Array({cd:"0", label:"選択してください"});
array["未選択"] = [{cd:"1", label:"未選択"}];
array["水産・農林業"] = [{cd:"1", label:"水産・農林業"}];
array["メーカー"] = [
    {cd:"1", label:"食品"},
    {cd:"2", label:"化粧品"},
    {cd:"3", label:"医薬品"},
    {cd:"4", label:"化学"},
    {cd:"5", label:"文具・事務機器・インテリア"},
    {cd:"6", label:"アパレル・服飾・雑貨・皮革製品"},
    {cd:"7", label:"印刷"},
    {cd:"8", label:"自動車"},
    {cd:"9", label:"輸送機器"},
    {cd:"10", label:"機械"},
    {cd:"11", label:"プラント・エンジニアリング"},
    {cd:"12", label:"総合電機（電気・電子機器）"},
    {cd:"13", label:"医療機器"},
    {cd:"14", label:"精密機器"},
    {cd:"15", label:"重電・産業用電気機器"},
    {cd:"16", label:"コンピュータ・通信機器・オフィス機器"},
    {cd:"17", label:"半導体・電子部品・その他"},
    {cd:"18", label:"家電・オーディオ機器"},
    {cd:"19", label:"ゲーム・アミューズメント機器"},
    {cd:"20", label:"住宅"},
    {cd:"21", label:"建設"},
    {cd:"22", label:"設備・設備工事"},
    {cd:"23", label:"建材・エクステリア"},
    {cd:"24", label:"繊維"},
    {cd:"25", label:"紙・パルプ"},
    {cd:"26", label:"ガラス・セラミックス"},
    {cd:"27", label:"タイヤ・ゴム製品"},
    {cd:"28", label:"石油・石炭"},
    {cd:"29", label:"金属製品"},
    {cd:"30", label:"非鉄金属"},
    {cd:"31", label:"鉄鋼・鉱業・セメント"},
    {cd:"32", label:"農業・農林"},
    {cd:"33", label:"水産"},
    {cd:"34", label:"その他メーカー"}
];
array["サービス・インフラ"] = [
    {cd:"1", label:"旅行"},
    {cd:"2", label:"ホテル"},
    {cd:"3", label:"レジャー・アミューズメント・パチンコ"},
    {cd:"4", label:"芸能・芸術"},
    {cd:"5", label:"スポーツ・フィットネス・ヘルス関連施設"},
    {cd:"6", label:"教育"},
    {cd:"7", label:"不動産"},
    {cd:"8", label:"公社・官庁"},
    {cd:"9", label:"航空・空港"},
    {cd:"10", label:"鉄道"},
    {cd:"11", label:"海運"},
    {cd:"12", label:"陸運"},
    {cd:"13", label:"タクシー・バス・観光バス"},
    {cd:"14", label:"倉庫"},
    {cd:"15", label:"電力・電気"},
    {cd:"16", label:"エネルギー"},
    {cd:"17", label:"ガス・水道"},
    {cd:"18", label:"外食・レストラン・フードサービス"},
    {cd:"19", label:"人材サービス（人材紹介・人材派遣）"},
    {cd:"20", label:"コンサルタント・専門コンサルタント"},
    {cd:"21", label:"建設コンサルタント"},
    {cd:"22", label:"シンクタンク"},
    {cd:"23", label:"医療関連・医療機関"},
    {cd:"24", label:"福祉・介護"},
    {cd:"25", label:"ブライダル・冠婚葬祭"},
    {cd:"26", label:"エステ・理容・美容"},
    {cd:"27", label:"団体・連合会"},
    {cd:"28", label:"建築設計"},
    {cd:"29", label:"警備・安全・メンテナンス・清掃"},
    {cd:"30", label:"機械設計"},
    {cd:"31", label:"その他サービス・インフラ"}
];
array["商社（総合・専門）"] = [
    {cd:"1", label:"総合商社"},
    {cd:"2", label:"食料品"},
    {cd:"3", label:"化学製品"},
    {cd:"4", label:"化粧品"},
    {cd:"5", label:"医薬品"},
    {cd:"6", label:"繊維製品"},
    {cd:"7", label:"アパレル・服飾雑貨・貴金属"},
    {cd:"8", label:"インテリア"},
    {cd:"9", label:"スポーツ用品"},
    {cd:"10", label:"教育"},
    {cd:"11", label:"機械"},
    {cd:"12", label:"医療機器"},
    {cd:"13", label:"自動車・輸送機器"},
    {cd:"14", label:"事務機器・オフィス機器"},
    {cd:"15", label:"電機・電子・半導体"},
    {cd:"16", label:"金属"},
    {cd:"17", label:"建材・エクステリア"},
    {cd:"18", label:"紙"},
    {cd:"19", label:"石油製品"},
    {cd:"20", label:"その他商社（総合・専門）"}
];
array["銀行・証券・保険・金融"] = [
    {cd:"1", label:"都市銀行・信託銀行"},
    {cd:"2", label:"地方銀行"},
    {cd:"3", label:"信用金庫・信用組合・労働金庫・共済"},
    {cd:"4", label:"外資系金融"},
    {cd:"5", label:"政府系・系統金融機関"},
    {cd:"6", label:"生命保険"},
    {cd:"7", label:"損害保険"},
    {cd:"8", label:"証券"},
    {cd:"9", label:"クレジット・信販"},
    {cd:"10", label:"リース・レンタル"},
    {cd:"11", label:"消費者金融"},
    {cd:"12", label:"その他銀行・証券・保険・金融"}
];
array["情報（広告・通信・マスコミ）"] = [
    {cd:"1", label:"広告"},
    {cd:"2", label:"出版・雑誌"},
    {cd:"3", label:"放送・テレビ・ラジオ"},
    {cd:"4", label:"新聞"},
    {cd:"5", label:"通信"}
];
array["百貨店・専門店・流通・小売"] = [
    {cd:"1", label:"百貨店・デパート・複合商業施設"},
    {cd:"2", label:"スーパー・ストア"},
    {cd:"3", label:"コンビニエンスストア"},
    {cd:"4", label:"ホームセンター"},
    {cd:"5", label:"生活協同組合"},
    {cd:"6", label:"音楽・書籍・インテリア"},
    {cd:"7", label:"ファッション・服飾雑貨・繊維"},
    {cd:"8", label:"ドラッグストア・医薬品・化粧品・調剤薬局"},
    {cd:"9", label:"スポーツ用品"},
    {cd:"10", label:"自動車・輸送機器"},
    {cd:"11", label:"家電・事務機器・カメラ"},
    {cd:"12", label:"メガネ・コンタクト・医療機器"},
    {cd:"13", label:"その他百貨店・専門店・流通・小売"}
];
array["IT・ソフトウェア・情報処理"] = [
    {cd:"1", label:"情報処理"},
    {cd:"2", label:"ソフトウェア"},
    {cd:"3", label:"インターネット・WEB・スマートフォンアプリ"},
    {cd:"4", label:"ゲームソフト"}
];

window.addEventListener('load',()=>{
    document.getElementById('business').onchange = function(){
        let occupation = document.getElementById("occupation");
        occupation.options.length = 0;
        let businessPref = document.getElementById('business').value;
        let occupationValue;
        for (let i = 0; i < array[businessPref].length; i++) {
            let op = document.createElement('option');
            occupationValue = array[businessPref][i];
            op.value = occupationValue.cd;
            op.text = occupationValue.label;
            occupation.appendChild(op);
        }
    }

});
