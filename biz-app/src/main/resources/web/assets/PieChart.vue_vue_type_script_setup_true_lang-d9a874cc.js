import{d as s,o as d,a as l,l as c,w as a,a3 as u,k as m,n as h,au as p}from"./index-d5547550.js";import{E as g}from"./el-card-39a9c0d0.js";import{i as f}from"./index-21782bc0.js";const y=["id"],S=s({__name:"PieChart",props:{id:{type:String,default:"pieChart"},className:{type:String,default:""},width:{type:String,default:"200px",required:!0},height:{type:String,default:"200px",required:!0}},setup(e){const i=e,n={grid:{left:"2%",right:"2%",bottom:"10%",containLabel:!0},legend:{top:"bottom",textStyle:{color:"#999"}},series:[{name:"Nightingale Chart",type:"pie",radius:[50,130],center:["50%","50%"],roseType:"area",itemStyle:{borderRadius:1,color:function(t){return["#409EFF","#67C23A","#E6A23C","#F56C6C"][t.dataIndex]}},data:[{value:26,name:"家用电器"},{value:27,name:"户外运动"},{value:24,name:"汽车用品"},{value:23,name:"手机数码"}]}]};return d(()=>{const t=f(document.getElementById(i.id));t.setOption(n),window.addEventListener("resize",()=>{t.resize()})}),(t,r)=>{const o=g;return l(),c(o,null,{header:a(()=>[u(" 产品分类饼图 ")]),default:a(()=>[m("div",{id:e.id,class:h(e.className),style:p({height:e.height,width:e.width})},null,14,y)]),_:1})}}});export{S as _};
