import{d as _,a4 as m,aE as i,a as g,l as f,w as e,a5 as n,f as c,a3 as t,k as w,a7 as d}from"./index-d5547550.js";import{b as h,E,a as S}from"./el-dropdown-item-15a84c23.js";import"./el-popper-00977718.js";import"./el-scrollbar-cbe0c54f.js";import{S as b}from"./index-f9f36d5d.js";const L=_({__name:"index",setup(x){const o=m(),{locale:l}=i();function r(a){l.value=a,o.changeLanguage(a),a=="en"?d.success("Switch Language Successful!"):d.success("切换语言成功！")}return(a,k)=>{const s=h,u=E,p=S;return g(),f(p,{trigger:"click",onCommand:r},{dropdown:e(()=>[n(u,null,{default:e(()=>[n(s,{disabled:c(o).language==="zh-cn",command:"zh-cn"},{default:e(()=>[t(" 中文 ")]),_:1},8,["disabled"]),n(s,{disabled:c(o).language==="en",command:"en"},{default:e(()=>[t(" English ")]),_:1},8,["disabled"])]),_:1})]),default:e(()=>[w("div",null,[n(b,{"icon-class":"language"})])]),_:1})}}});export{L as _};
