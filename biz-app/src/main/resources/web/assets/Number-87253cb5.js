import{d as _,D as V,h as b,a as g,c as v,a5 as r,w as p,f as o,aA as x}from"./index-e01dade3.js";import{E as c}from"./el-input-number-182b17d2.js";import{a as y}from"./el-form-item-85be6a3a.js";import{u as w}from"./flow-2349aa77.js";const N=_({__name:"Number",props:{id:{type:String,default:""}},setup(m){const s=m;let d=w();var t=V(()=>{let i=d.step2;var l=i.filter(e=>e.id===s.id);if(l.length>0)return l[0];let u=i.filter(e=>e.type==="Layout");for(var n of u){var a=n.props.value.filter(f=>f.id===s.id);if(a.length>0)return a[0]}});return b(),(i,l)=>{const u=x,n=y,a=c;return g(),v("div",null,[r(n,{label:"单位"},{default:p(()=>[r(u,{modelValue:o(t).props.unit,"onUpdate:modelValue":l[0]||(l[0]=e=>o(t).props.unit=e),style:{width:"100%"},maxlength:"10"},null,8,["modelValue"])]),_:1}),r(n,{label:"最小值"},{default:p(()=>[r(a,{modelValue:o(t).props.min,"onUpdate:modelValue":l[1]||(l[1]=e=>o(t).props.min=e),style:{width:"100%"},"controls-position":"right",min:1,max:1e14},null,8,["modelValue"])]),_:1}),r(n,{label:"最大值"},{default:p(()=>[r(a,{modelValue:o(t).props.max,"onUpdate:modelValue":l[2]||(l[2]=e=>o(t).props.max=e),style:{width:"100%"},"controls-position":"right",min:1,max:1e14},null,8,["modelValue"])]),_:1}),r(n,{label:"小数位数"},{default:p(()=>[r(a,{step:1,"step-strictly":"",modelValue:o(t).props.radixNum,"onUpdate:modelValue":l[3]||(l[3]=e=>o(t).props.radixNum=e),style:{width:"100%"},"controls-position":"right",min:0,"value-on-clear":"min",max:10},null,8,["modelValue"])]),_:1}),r(n,{label:"默认值"},{default:p(()=>[r(a,{precision:o(t).props.radixNum,"controls-position":"right",style:{width:"100%"},modelValue:o(t).props.value,"onUpdate:modelValue":l[4]||(l[4]=e=>o(t).props.value=e)},null,8,["precision","modelValue"])]),_:1})])}}}),I=Object.freeze(Object.defineProperty({__proto__:null,default:N},Symbol.toStringTag,{value:"Module"}));export{I as _};
