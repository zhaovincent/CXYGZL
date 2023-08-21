import{d as J,o as z,r as y,h as F,a as p,c as _,a5 as s,w as r,a3 as c,R as k,a6 as x,f as w,k as i,l as h,am as S,as as Q,a7 as m,aD as j,aC as G}from"./index-d5547550.js";/* empty css                  */import{E as H}from"./el-card-39a9c0d0.js";import{E as K}from"./el-input-number-f4eeab94.js";import{E as P}from"./el-switch-a81630c6.js";import"./el-tag-de6752b8.js";import{E as W,a as X}from"./el-select-46753b34.js";import"./el-scrollbar-cbe0c54f.js";import"./el-popper-00977718.js";import{_ as Y}from"./plus-bafa12a0.js";import{q as Z,s as ee}from"./index-6e9421a4.js";import{E as le}from"./index-a6355ec2.js";import{_ as te}from"./_plugin-vue_export-helper-c27b6911.js";import"./index-da53d962.js";import"./strings-fbdcd853.js";import"./isEqual-fe47b28a.js";import"./_Uint8Array-c7447712.js";import"./debounce-e2bc871f.js";import"./index-e8e7f773.js";import"./use-dialog-27a1e524.js";import"./refs-5fd04da2.js";const ae={class:"app-container"},oe={style:{display:"flex","flex-direction":"row","margin-bottom":"20px"}},ne={class:"f1"},se={class:"f2"},re={class:"f3"},ue={class:"f4"},pe={class:"f5"},ie={style:{display:"flex","flex-direction":"row","margin-bottom":"10px"}},de={class:"f21"},ce={class:"f22"},me={class:"f23"},_e={class:"dialog-footer"},ve=J({__name:"index",setup(fe){function E(){Z().then(o=>{let n=o.data;n.forEach(a=>a.props=JSON.parse(a.props)),u.value=n})}z(()=>{E(),C.value=[{value:"Number",label:"数字"},{value:"Input",label:"单行文本"},{value:"Textarea",label:"多行文本"},{value:"Date",label:"日期"},{value:"DateTime",label:"日期时间"},{value:"Time",label:"时间"},{value:"SingleSelect",label:"单选"},{value:"MultiSelect",label:"多选"}]});const C=y([]),u=y([]),{proxy:f}=F(),O=()=>{u.value.push({required:!1,name:"",type:"",perm:"E",key:f.$getRandomId(),props:{options:[],radixNum:4}})},B=o=>{u.value.splice(o,1)},U=y(),$=o=>{U.value=o;let a=u.value[o].props.options;a.length==0?N():d.value=a,v.value=!0},v=y(!1),N=()=>{d.value.push({key:"",value:""})},d=y([]),D=o=>{d.value.splice(o,1)},L=()=>{{var o=d.value.filter(a=>f.$isBlank(a.key)||f.$isBlank(a.value)).length;if(o>0){m.warning("所有的值必填");return}}{var n=d.value.map(t=>t.key);if(Array.from(new Set(n)).length!=n.length){m.warning("选项键值必须唯一");return}}{var n=d.value.map(g=>g.value);if(Array.from(new Set(n)).length!=n.length){m.warning("选项名必须唯一");return}}u.value[U.value].props.options=d.value,v.value=!1},M=()=>{{var o=u.value.filter(a=>f.$isBlank(a.type)).length;if(o>0){m.warning("属性类型不能空");return}}{var o=u.value.filter(t=>f.$isBlank(t.name)).length;if(o>0){m.warning("属性名称不能空");return}}{var o=u.value.filter(t=>(t.type=="SingleSelect"||t.type==="MultiSelect")&&t.props.options.length==0).length;if(o>0){m.warning("单选/多选的选项不能空");return}}{var n=u.value.map(t=>t.name);if(Array.from(new Set(n)).length!=n.length){m.warning("属性名称不唯一");return}}u.value.forEach(a=>a.props=JSON.stringify(a.props)),ee(u.value).then(a=>{m.success("保存成功"),E()})};return(o,n)=>{const a=Y,t=j,g=W,I=X,b=G,q=P,A=K,T=H,R=le;return p(),_("div",ae,[s(T,{shadow:"never"},{header:r(()=>[s(t,{type:"success",onClick:O},{default:r(()=>[s(a),c(" 新增 ")]),_:1})]),default:r(()=>[(p(!0),_(k,null,x(w(u),(e,V)=>(p(),_("div",oe,[i("div",ne,[s(I,{modelValue:e.type,"onUpdate:modelValue":l=>e.type=l,placeholder:"选择属性类型"},{default:r(()=>[(p(!0),_(k,null,x(w(C),l=>(p(),h(g,{key:l.value,label:l.label,value:l.value},null,8,["label","value"]))),128))]),_:2},1032,["modelValue","onUpdate:modelValue"])]),i("div",se,[s(b,{modelValue:e.name,"onUpdate:modelValue":l=>e.name=l,placeholder:"输入属性名称"},null,8,["modelValue","onUpdate:modelValue"])]),i("div",re,[s(q,{modelValue:e.required,"onUpdate:modelValue":l=>e.required=l,"active-text":"必填","inactive-text":"非必填"},null,8,["modelValue","onUpdate:modelValue"])]),i("div",ue,[e.type==="SingleSelect"||e.type==="MultiSelect"?(p(),h(I,{key:0,placeholder:"选项列"},{default:r(()=>[(p(!0),_(k,null,x(e.props.options,l=>(p(),h(g,{key:l.key,label:l.value,value:l.key},null,8,["label","value"]))),128))]),_:2},1024)):S("",!0),e.type==="Number"?(p(),h(A,{key:1,step:2,"step-strictly":"",min:0,max:10,"value-on-clear":"min",modelValue:e.props.radixNum,"onUpdate:modelValue":l=>e.props.radixNum=l},null,8,["modelValue","onUpdate:modelValue"])):S("",!0)]),i("div",pe,[s(t,{type:"primary",text:"",onClick:l=>B(V)},{default:r(()=>[c(" 删除")]),_:2},1032,["onClick"]),e.type==="SingleSelect"||e.type==="MultiSelect"?(p(),h(t,{key:0,type:"primary",onClick:l=>$(V),text:""},{default:r(()=>[c(" 添加选项")]),_:2},1032,["onClick"])):S("",!0)])]))),256)),i("div",null,[s(t,{type:"primary",onClick:M,size:"large"},{default:r(()=>[c("保存")]),_:1})])]),_:1}),s(R,{modelValue:w(v),"onUpdate:modelValue":n[1]||(n[1]=e=>Q(v)?v.value=e:null),title:"添加选项",width:"30%"},{footer:r(()=>[i("span",_e,[s(t,{type:"success",text:"",onClick:N},{default:r(()=>[c(" 添加选项")]),_:1}),s(t,{onClick:n[0]||(n[0]=e=>v.value=!1)},{default:r(()=>[c("取消")]),_:1}),s(t,{type:"primary",onClick:L},{default:r(()=>[c(" 确定 ")]),_:1})])]),default:r(()=>[i("span",null,[(p(!0),_(k,null,x(w(d),(e,V)=>(p(),_("div",ie,[i("div",de,[s(b,{modelValue:e.key,"onUpdate:modelValue":l=>e.key=l,placeholder:"选项唯一值"},null,8,["modelValue","onUpdate:modelValue"])]),i("div",ce,[s(b,{modelValue:e.value,"onUpdate:modelValue":l=>e.value=l,placeholder:"选项名"},null,8,["modelValue","onUpdate:modelValue"])]),i("div",me,[s(t,{type:"danger",onClick:l=>D(V),text:""},{default:r(()=>[c("删除")]),_:2},1032,["onClick"])])]))),256))])]),_:1},8,["modelValue"])])}}});const Ae=te(ve,[["__scopeId","data-v-259fe822"]]);export{Ae as default};
