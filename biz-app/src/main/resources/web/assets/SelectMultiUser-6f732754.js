import{d as V,D as f,h as k,r as C,f as l,a as S,c as w,a5 as i,as as d,w as v,e3 as x,ae as U,k as h,am as E,aD as B}from"./index-d5547550.js";import{a as D}from"./el-form-item-2de9c3c8.js";/* empty css                    */import j from"./employeesDialog-54936475.js";import I from"./orgItem-a67c9471.js";import{u as M}from"./flow-165b4256.js";import{E as N}from"./index-222e6800.js";const O={key:0},F={style:{width:"100%"}},z=V({__name:"SelectMultiUser",props:{id:{type:String,default:""}},setup(_,{expose:c}){const m=_;let b=M();var s=f(()=>{let r=b.step2;var t=r.filter(e=>e.id===m.id);if(t.length>0)return t[0];let p=r.filter(e=>e.type==="Layout");for(var n of p){var u=n.props.value.filter(y=>y.id===m.id);if(u.length>0)return u[0]}}),a=f({get:()=>s.value.props.value,set:r=>{s.value.props.value=r}});k(),c({validate:()=>!0});let o=C(!1);const g=r=>{a.value=r,o.value=!1};return(r,t)=>{const p=N,n=D,u=B;return l(s)?(S(),w("div",O,[i(j,{visible:l(o),"onUpdate:visible":t[0]||(t[0]=e=>d(o)?o.value=e:o=e),data:l(a),type:"user",multiple:l(s).props.multi,onChange:g},null,8,["visible","data","multiple"]),i(n,{label:"选择范围"},{default:v(()=>[i(p,{modelValue:l(s).props.self,"onUpdate:modelValue":t[1]||(t[1]=e=>l(s).props.self=e),disabled:l(a).length>0,label:"可选自己"},null,8,["modelValue","disabled"])]),_:1}),i(n,{label:"默认值"},{default:v(()=>[i(u,{circle:"",icon:l(x),onClick:t[2]||(t[2]=U(e=>d(o)?o.value=!0:o=!0,["stop"]))},null,8,["icon"]),h("div",F,[i(I,{data:l(a),"onUpdate:data":t[3]||(t[3]=e=>d(a)?a.value=e:a=e)},null,8,["data"])])]),_:1})])):E("",!0)}}}),J=Object.freeze(Object.defineProperty({__proto__:null,default:z},Symbol.toStringTag,{value:"Module"}));export{J as _};
