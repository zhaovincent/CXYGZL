import{d as _,D as c,h as v,a as g,c as x,a5 as l,w as p,f as r}from"./index-e01dade3.js";import{a as b}from"./el-form-item-85be6a3a.js";import{E as V}from"./el-input-number-182b17d2.js";import{u as y}from"./flow-2349aa77.js";const w=_({__name:"UploadImage",props:{id:{type:String,default:""}},setup(u){const s=u;let d=y();var o=c(()=>{let m=d.step2;var e=m.filter(i=>i.id===s.id);if(e.length>0)return e[0];let a=m.filter(i=>i.type==="Layout");for(var n of a){var t=n.props.value.filter(f=>f.id===s.id);if(t.length>0)return t[0]}});return v(),(m,e)=>{const a=V,n=b;return g(),x("div",null,[l(n,{label:"最小数量"},{default:p(()=>[l(a,{modelValue:r(o).props.min,"onUpdate:modelValue":e[0]||(e[0]=t=>r(o).props.min=t),style:{width:"100%"},"value-on-clear":"min","controls-position":"right",min:1,max:10},null,8,["modelValue"])]),_:1}),l(n,{label:"最大数量"},{default:p(()=>[l(a,{modelValue:r(o).props.max,"onUpdate:modelValue":e[1]||(e[1]=t=>r(o).props.max=t),style:{width:"100%"},"value-on-clear":"max","controls-position":"right",min:1,max:10},null,8,["modelValue"])]),_:1}),l(n,{label:"文件大小(MB)"},{default:p(()=>[l(a,{modelValue:r(o).props.maxSize,"onUpdate:modelValue":e[2]||(e[2]=t=>r(o).props.maxSize=t),style:{width:"100%"},"controls-position":"right",min:.01,"value-on-clear":"max",max:10},null,8,["modelValue"])]),_:1})])}}}),U=Object.freeze(Object.defineProperty({__proto__:null,default:w},Symbol.toStringTag,{value:"Module"}));export{U as _};