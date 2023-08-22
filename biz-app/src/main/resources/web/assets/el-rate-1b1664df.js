import{b as me,g as S,Y as K,em as V,i as L,en as pe,d_ as he,aS as be,aT as $,bw as C,d as ae,j as W,bG as ye,bg as Ve,br as we,u as xe,bz as Ce,r as I,D as u,bi as E,Z as q,da as T,O as G,L as ge,a as c,c as h,R as Q,a6 as _e,n as b,f as o,a5 as Se,w as U,P as X,l as w,m as D,Q as Y,am as N,E as Z,au as J,q as Ie,_ as Ee,x,C as ee,s as Te}from"./index-d5547550.js";const De=me({modelValue:{type:Number,default:0},id:{type:String,default:void 0},lowThreshold:{type:Number,default:2},highThreshold:{type:Number,default:4},max:{type:Number,default:5},colors:{type:S([Array,Object]),default:()=>K(["","",""])},voidColor:{type:String,default:""},disabledVoidColor:{type:String,default:""},icons:{type:S([Array,Object]),default:()=>[V,V,V]},voidIcon:{type:L,default:()=>pe},disabledVoidIcon:{type:L,default:()=>V},disabled:Boolean,allowHalf:Boolean,showText:Boolean,showScore:Boolean,textColor:{type:String,default:""},texts:{type:S(Array),default:()=>K(["Extremely bad","Disappointed","Fair","Satisfied","Surprise"])},scoreTemplate:{type:String,default:"{value}"},size:he,label:{type:String,default:void 0},clearable:{type:Boolean,default:!1}}),Ne={[be]:v=>$(v),[C]:v=>$(v)},ke=["id","aria-label","aria-labelledby","aria-valuenow","aria-valuetext","aria-valuemax"],He=["onMousemove","onClick"],Me=ae({name:"ElRate"}),Be=ae({...Me,props:De,emits:Ne,setup(v,{expose:le,emit:m}){const e=v;function g(a,l){const t=n=>q(n),i=Object.keys(l).map(n=>+n).filter(n=>{const y=l[n];return(t(y)?y.excluded:!1)?a<n:a<=n}).sort((n,y)=>n-y),p=l[i[0]];return t(p)&&p.value||p}const k=W(ye,void 0),H=W(Ve,void 0),oe=we(),r=xe("rate"),{inputId:te,isLabeledByFormItem:M}=Ce(e,{formItemContext:H}),s=I(e.modelValue),_=I(-1),f=I(!0),se=u(()=>[r.b(),r.m(oe.value)]),d=u(()=>e.disabled||(k==null?void 0:k.disabled)),re=u(()=>r.cssVarBlock({"void-color":e.voidColor,"disabled-void-color":e.disabledVoidColor,"fill-color":A.value})),B=u(()=>{let a="";return e.showScore?a=e.scoreTemplate.replace(/\{\s*value\s*\}/,d.value?`${e.modelValue}`:`${s.value}`):e.showText&&(a=e.texts[Math.ceil(s.value)-1]),a}),P=u(()=>e.modelValue*100-Math.floor(e.modelValue)*100),ne=u(()=>E(e.colors)?{[e.lowThreshold]:e.colors[0],[e.highThreshold]:{value:e.colors[1],excluded:!0},[e.max]:e.colors[2]}:e.colors),A=u(()=>{const a=g(s.value,ne.value);return q(a)?"":a}),ue=u(()=>{let a="";return d.value?a=`${P.value}%`:e.allowHalf&&(a="50%"),{color:A.value,width:a}}),O=u(()=>{let a=E(e.icons)?[...e.icons]:{...e.icons};return a=T(a),E(a)?{[e.lowThreshold]:a[0],[e.highThreshold]:{value:a[1],excluded:!0},[e.max]:a[2]}:a}),ie=u(()=>g(e.modelValue,O.value)),de=u(()=>d.value?G(e.disabledVoidIcon)?e.disabledVoidIcon:T(e.disabledVoidIcon):G(e.voidIcon)?e.voidIcon:T(e.voidIcon)),ce=u(()=>g(s.value,O.value));function j(a){const l=d.value&&P.value>0&&a-1<e.modelValue&&a>e.modelValue,t=e.allowHalf&&f.value&&a-.5<=s.value&&a>s.value;return l||t}function z(a){e.clearable&&a===e.modelValue&&(a=0),m(C,a),e.modelValue!==a&&m("change",a)}function fe(a){d.value||(e.allowHalf&&f.value?z(s.value):z(a))}function ve(a){if(d.value)return;let l=s.value;const t=a.code;return t===x.up||t===x.right?(e.allowHalf?l+=.5:l+=1,a.stopPropagation(),a.preventDefault()):(t===x.left||t===x.down)&&(e.allowHalf?l-=.5:l-=1,a.stopPropagation(),a.preventDefault()),l=l<0?0:l,l=l>e.max?e.max:l,m(C,l),m("change",l),l}function F(a,l){if(!d.value){if(e.allowHalf&&l){let t=l.target;ee(t,r.e("item"))&&(t=t.querySelector(`.${r.e("icon")}`)),(t.clientWidth===0||ee(t,r.e("decimal")))&&(t=t.parentNode),f.value=l.offsetX*2<=t.clientWidth,s.value=f.value?a-.5:a}else s.value=a;_.value=a}}function R(){d.value||(e.allowHalf&&(f.value=e.modelValue!==Math.floor(e.modelValue)),s.value=e.modelValue,_.value=-1)}return ge(()=>e.modelValue,a=>{s.value=a,f.value=e.modelValue!==Math.floor(e.modelValue)}),e.modelValue||m(C,0),le({setCurrentValue:F,resetCurrentValue:R}),(a,l)=>{var t;return c(),h("div",{id:o(te),class:b([o(se),o(r).is("disabled",o(d))]),role:"slider","aria-label":o(M)?void 0:a.label||"rating","aria-labelledby":o(M)?(t=o(H))==null?void 0:t.labelId:void 0,"aria-valuenow":s.value,"aria-valuetext":o(B)||void 0,"aria-valuemin":"0","aria-valuemax":a.max,tabindex:"0",style:J(o(re)),onKeydown:ve},[(c(!0),h(Q,null,_e(a.max,(i,p)=>(c(),h("span",{key:p,class:b(o(r).e("item")),onMousemove:n=>F(i,n),onMouseleave:R,onClick:n=>fe(i)},[Se(o(Z),{class:b([o(r).e("icon"),{hover:_.value===i},o(r).is("active",i<=s.value)])},{default:U(()=>[j(i)?N("v-if",!0):(c(),h(Q,{key:0},[X((c(),w(D(o(ce)),null,null,512)),[[Y,i<=s.value]]),X((c(),w(D(o(de)),null,null,512)),[[Y,!(i<=s.value)]])],64)),j(i)?(c(),w(o(Z),{key:1,style:J(o(ue)),class:b([o(r).e("icon"),o(r).e("decimal")])},{default:U(()=>[(c(),w(D(o(ie))))]),_:1},8,["style","class"])):N("v-if",!0)]),_:2},1032,["class"])],42,He))),128)),a.showText||a.showScore?(c(),h("span",{key:0,class:b(o(r).e("text"))},Ie(o(B)),3)):N("v-if",!0)],46,ke)}}});var Pe=Ee(Be,[["__file","/home/runner/work/element-plus/element-plus/packages/components/rate/src/rate.vue"]]);const je=Te(Pe);export{je as E};