import{d as y,D as m,h as S,r as w,f as r,a as C,c as k,a5 as n,as as d,w as D,e3 as V,ae as h,k as x,am as B,aD as U}from"./index-d5547550.js";import{a as j}from"./el-form-item-2de9c3c8.js";import E from"./employeesDialog-54936475.js";import I from"./orgItem-a67c9471.js";import{u as N}from"./flow-165b4256.js";const O={key:0},F={style:{width:"100%"}},M=y({__name:"SelectDept",props:{id:{type:String,default:""}},setup(v,{expose:c}){const f=v;let _=N();var s=m(()=>{let o=_.step2;var e=o.filter(i=>i.id===f.id);if(e.length>0)return e[0];let u=o.filter(i=>i.type==="Layout");for(var p of u){var t=p.props.value.filter(b=>b.id===f.id);if(t.length>0)return t[0]}}),l=m({get:()=>s.value.props.value,set:o=>{s.value.props.value=o}});S(),c({validate:()=>!0});let a=w(!1);const g=o=>{l.value=o,a.value=!1};return(o,e)=>{const u=U,p=j;return r(s)?(C(),k("div",O,[n(E,{visible:r(a),"onUpdate:visible":e[0]||(e[0]=t=>d(a)?a.value=t:a=t),data:r(l),type:"dept",multiple:r(s).props.multi,onChange:g},null,8,["visible","data","multiple"]),n(p,{label:"默认值"},{default:D(()=>[n(u,{circle:"",icon:r(V),onClick:e[1]||(e[1]=h(t=>d(a)?a.value=!0:a=!0,["stop"]))},null,8,["icon"]),x("div",F,[n(I,{data:r(l),"onUpdate:data":e[2]||(e[2]=t=>d(l)?l.value=t:l=t)},null,8,["data"])])]),_:1})])):B("",!0)}}}),A=Object.freeze(Object.defineProperty({__proto__:null,default:M},Symbol.toStringTag,{value:"Module"}));export{A as _};