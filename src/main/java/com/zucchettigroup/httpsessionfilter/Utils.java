package com.zucchettigroup.httpsessionfilter;

import java.io.File;
import java.net.URISyntaxException;

public class Utils 
{
    public static File getRootFolder() 
    {
        try 
        {
            File root;
            String runningJarPath = 
            		Utils.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath().replaceAll("\\\\", "/");
            int lastIndexOf = runningJarPath.lastIndexOf("/target/");
            if (lastIndexOf < 0) 
            {
                root = new File("");
            }
            else 
            {
                root = new File(runningJarPath.substring(0, lastIndexOf));
            }
            System.out.println("application resolved root folder: " + root.getAbsolutePath());
            return root;
        }
        catch (URISyntaxException ex) 
        {
            throw new RuntimeException(ex);
        }
    }
    
    public static String logoImageEncodedTag()
    {
    	return "<img src=\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAZAAAABiCAYAAAB+koVqAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAEsRJRE"
    			+ "FUeNrsnQe4FcUVx4eiFAVBsWGPlRBULNi/WECM3sSoET8D6mfPJWpMJLEroMEeTTRejYrYNVZ0LRhFVGLvxoYiRVEsKKCigPhy/nfnmevLvXdndnd29+79/75v9"
    			+ "r3vvS1nZ3fnzMyZc45SthS9g6VspQghhDQ17UMcs5yUCaJE+rH6CCGECiSMEplIJUIIIVQgNiyUMk/KUlLuECWyHquREEIIIYQQYkQ76yOKXnfZLl/lPx+qUmEh"
    			+ "q5QQQpqDMFNYh0iZVqXcLcplaVYpIYRQgdSi1QbStmBp75WiRNqzWgkhhBBCCCFVidMGUsliVSrMYvUSQkh+idMGUlneE0UzlNVLCCH5pWOIY+ZLmWGw32hRIli"
    			+ "ZNZHVTAghhBBCCCkTxgayomxXszxqpoxEPmN1E0JIfggzhQXbxoWWx0wVxbOjKJH3WeWEENK8CsTUBtL2OuNEiewtSmQ+q50QQgghhJAmJSkbSCUtUl6RkUgLq5"
    			+ "8QQhqXpGwgbSlJGc7qJ4SQ5lIgYWwgbdldRjJFGYWU+AgIIYQQQghpItrJSGDTlGX4TEYiM/koCCGk8RRI2sbsr6UMFiXyOB8HIYQ0lgJ5KQNyIJ/I0NQcDYveU"
    			+ "nLtxXwdQtXdSrLtKaWL8lf1fSflcykfhc5QWfQ6ybb1vB2Uv3JvQfm8pcInDu8FKww3l7KulDX1PSH69FcKEaaVwrURZRoj5pecRFcoeqjDH+uynq4HyNFJfyeL"
    			+ "ynXryzBV+Ssav7W8Bmyfqys/qnZ3/Vfc4xdSpsv5vgkhdy/ZrqrPt5SUb/X5EA/vY34oDfltd5btWvqZLlPRVs+V8j7azHasJG9b2W4vlXFuA8i6n2wvT/CKUAY"
    			+ "DpG7eqSILOh59pNTLQomG40g5/i7D+/ulvr+V6uwFpfS0nPOnMdXpBrI9UAqu3dfy6DekPCLldimTRKbvQsrQoTwKV+oAKQOl9LI4ekG5PpR6UMqNdaeDi1432b"
    			+ "6m/GX49SJxQ0EV5FzPGch+tmyPqmhgas0yjJPzDa9znjXLytAtR4kM17e57omyPT6Fr3knkeXFCjk+kG3XhGV4QmTYvcqz2EK2npSVA9qGWR3LIUbc8YwIuCDDD"
    			+ "TJ6efdIubhB1B0auOUSviY+rsOr/H3tAOWhtCLoYXGtHgHKQ+me+FoxPPv+sj1dyp4RztJHl+HlnnbRGyLv+2TLkcAwKafoUU8YupYbI7+cJed8VGSo9U1DUa1h"
    			+ "cE40HMsaXn+VAOWh9Aiqd8A+7RN4t6t1EDZL4ZtqfXderFCeq6YgwyY1/r5sgPJofV5rdNQ9KBeg17l3hpUHhvATVHByrCyxYQrXfD5nI058HGN0rznOEfiquoG"
    			+ "2UWBXSekf8x2upkgtqinpDVKSZZ2Uv2tVVupFr2uUTj4UyMsOBMMc2SGZ9TaHzUOpc6TM0WV2g3wASQ9xMcc+NkfKY33Zjte9PxfMNJQDI7q/K99WkI4MzUm1kc"
    			+ "YKKcnSs+L3NDuxsG9EUCClwqZN9xr5BvNGzJjYLeHrnSl1tSgnyqOfbJHcrJejK2DRwDQDOUbL9lSHd/ok9URNOlX527IpydKlxu9J0znKwS5sIE+FWsWRTCOCK"
    			+ "Ybtq0xdYOXJ9Ab4AL5O8FpTpFyfo5GHS+UBHjKQ4zjHygM8TD1Rk2qdoQUqHRtIZRu5MMU6iXTtuG0gN0lDPCmjjQiUxhVSDq7y31FSRjbAB5DkgoTTrZeHZvO5"
    			+ "w8B7p2PlAcYHyLGrbF2v9MN07GRFajGvyt8w3Z6GAfvzNjKkRaT0GnHaQD6VckSGXx5MWW1W434bxQYyLaHrvC7lnzlpNGDr6uv4GmiY7q6jPLC67FpVf+lsXB0"
    			+ "4+jPVptosA5ao90lBlhltZEiDT+V9+SqaAmkWG4i//rvRp2ReT+g6J4f2acjW6GNLFT7q8wsK07F+o4NGuYsexWA6bID64TLHawOcJs9Wwcsiq/GllMd0p6fVGQ"
    			+ "/KCCsIN5KC9fqV8/pXUEfU5Z0a39TPU5BlasXv7yp/KqlTwjJMiXoC134gUXk3cpws39cjyLegUWwgNyt/Lj/M6OtMw33hIDg+Jw3GGcp+qS6W1o6R9+HdgPcKv"
    			+ "Vb4kOwl5a919oM3+WEhRvOwlVwjcnxd59wwgMKm96uyYikVXlHNwR4hO1PVPOKxpPsyg2PvMxyp4F24yGC/9ys6t0v0exIUHX3r8ijTDPh4BE1PRZ4Sd+kHEl15"
    			+ "KLVVROUxSLb3quDlko1hA/HXa0+3rAM4+x1qccRJuUj25XvTDrY4Ag31vnLv9xo+C3ihv6FHF/X4o7LxD4F3MPynSoWPDGSAIfYhZWLAzxcfxNbh81Nszzd4n0x"
    			+ "XI84NJZtJGKeit7bFGWfKOZ3bVlz5gUQFxtthUgGfRmhA4FB1mmFPZXaOP7ZD1A+dluqBECH35+S+bexx35VHE6XCv2JWYjDg/9riCIwgdhc55ilCGoD82kBKBQ"
    			+ "S926Gpn27R66Lslo2enJP7xqhrP4sjLohdefggvpapn8ESKftTeZDGUiB+ALssMaHuvG9w44E13TtZHvWmXPPNHD7fI1VwDKJWEEMpLz4EsAt0N9wX0xdnOpJjD"
    			+ "4t9x0r9v64IaSgF4q+RzwoXGEdura08EJV0gOWRjeIHYlMX6PmeZHHEaTm6+0EW+16n58FdMNBmzMzmiDSiAnk0I7K8paKHVT5W+cZQ23uansNni2CBK1qM+h7L"
    			+ "0b1vYbHvXU4k8COsmtb/zB+E9s7K+2M2OzEgBdlGi2ymuVj+IXX7BJt6VwqkdujnxqNUGKVHE82N77hmo4xH5qwGNjHcD3YHV7GjbGyLWczGuU+Gn6+N3wY6h1Q"
    			+ "gzhRI+jaQjyP3EIoebB5R4tnkzQaCkZhpHo7xcu9P5Uh5drbo+c+I6olbhzUt9n2VTZEzNmAVuFQg6dpAMN0ULbNc0TtBIZFONPJjA/FTi46wvPc8YdNwv+dQjj"
    			+ "Us9v2QTZEzerMK3CqQNG0gF0sP8NkIjSXmX3eL4R6m5+iZwnFtGcN9b83g3HtUbHKmzHcoR5eMyNHs9GAVuFQgjWwDKRWeke2OfIzfK1REFT3GcO8WlT/bB7DJ7"
    			+ "/CFQzm6W+w7ly+vM5awClwqkHRsIM9qR7+wDSVSU/ZLue4ezGC+9xOVeYKYG3Pqd/Ct1fvvjpaMyNHsUDk7VSDJ20Cw4mRg6KP9oGNYOdMr5bpDeJDpGRp9YO7/"
    			+ "Nxa9spE5fadtkpl1dyjHvIzI0ezMYhW4VSBJ2kCw4uXA0GlS/dAcaPhea7CGKgkQssQ0xzbCj7+Twfcxjpzvn2ek4bZZ3dWTTZEz6N3vVIE0kg3ED3EyjI+t6qj"
    			+ "sYMO9F8c0+jDNF2ITTn3pGOSyWdG0rsOnYrPCa+0MvlW3q8qQ47VBpsWkEzKdbzGyuJ8NhNsRiG1jZWN/6KobBYw4bg6dpOh/wfE6ZKjubpP7+TIjsoy0qJuxkX"
    			+ "Os+Mw37DnbrIIxVSAtdToZi+R9QcO3usF5VpZ9e8sxHzh4Ju9a7LtFBtuGS4zSUxe9cSkokBtEtpfYfDeiAvG9QC+0POaICMqjg/ITKe2VsbrDx5W+AvETZpmGD"
    			+ "Me0W1x+H6YBL21sVcsb7hc0TfWyoQIBsMdd6+DJ2CR32k6eY6eArIaE5EKBIBWjTca6J+TDiJJqE9nW2qvsZcnLygosm6x7l8uziMtpzdTWYJPK1VTZBMVBgm+R"
    			+ "aSTcYU4USKkw22Ik1E352Q3zkoeeUIHU/DDuke09iUlYKtwi21v4qKqOPvorZK8zA0bds2K8uukcdF+Lc5pOhQRNOSFD30jDcw2Setxc3rPnHTwh5BgxtU39SeS"
    			+ "4LRe56AkVSJ1GC1MmJhE434wUY6nobSPbDTNcd1mwgdjksbjEKE2qOTMM99u0HFo+qK78FXamAQinB/wf790cKSsYj8yK3rahVwfW5j4LBbK58iMo/43NEsmvAv"
    			+ "FXXQTZQD5UUcI8F71flBto82WpaTBJpWkDKXpbK6Q/NQM+CefHLIHpUmo4NsJ+dV3AfpjC6RrLtUuFJVI/Nyhzr3w03uPkmIPk2MUx1hFG6phuM7XtnC8yIMDje"
    			+ "EVIThWIiQ3kDKMk8dUbRuQyH6Z7b1kmbRuIzXTURZHyy1fnBYt9kb/h/poyFD00sH+2OJ/JCpzLpBytzO1D+ysEYix6h1lFZi56WJEIA/hzVRTZQvn/NfLb7w3P"
    			+ "hg7THXLMGPk5xjgzpx+BeFC5U1MqfKEIyawCcW0D8UOcDOGjqdtg7KhsY4AVvWMtr/JVwOKHVy1612srTCsVPWQ9nKgNzGjYYWDfRfn2ih8ZyvWBHP+WwXv0hlz"
    			+ "jDmWX12I7Bcezone38v0gEGtt2vdTW1gppRR8bmDX2VmPxhGRAAEpN6txzvNwpDIPMYMFI6copCMuehi1TVBYVVY5/Vj0MDW3oR7l4z3ASjIE0ByumiOz4VD9Dd"
    			+ "gyjaO7tBVIfRvIPfKA5mS44cUUyb7KzrmtFmnaQGyN4aeHuMZ35QasVPimRgON/6MjcZDh+eA/dIN+Dkv0M2gfQi6bBgApfQvlEYI5kGtPXVrfG4w2l67zvfQvT"
    			+ "ylWs/lh1VvRgxI51fI+kdPkD7pABmwxuuhW55jfNokCGRHyuFdV9lZzNpkCqW0DuVo+lnEZVh5oGK5X8fmTTFJp2ECK3s9ku3UCV0LjvpGqP110pYUCqSSKQ+hY"
    			+ "i9HsFKkv+L2MiVgXJraZoco33lcD03NYjh7V4a5bwP/7lqfUSgUmqKpOH6mfpWK2c1GBWFLNBoLh9dEZv9dBKl5/kuRtIL4SHJPgFXvXVSClwmSRCbHUfpqQPBO"
    			+ "q2hrqc47yp6b2cCzbPlIXv6u6DNe3hUCBPK3sws2HYYhihsN67R2m/2azKtJSIEn7gcRFqfCgQgj2xgah9zdN8HomYUgwxfKMch9mZlGoqQt/qm2ofvYDHMqHXC"
    			+ "wb11S4CJ1f9DAthrmoLg7lwAj1VEXqvdNUIKkpEN95rbXHOUc+jOsyfYe+vJvEeMbOuiCm1PwE76O9slupFAfBTm2lwgsiG/KQnOtYluPkWv8J2XmYJzLuqjs+O"
    			+ "ziUcZeAEdtEkQM2GRj3l3MkQ//yqrZS4TNFqtHCKkhTgfjKAzYQzCMOzrjywJzzI44+1rtUsqlIscw06aB1ZjktSoXz9Mqg4x3JMVqucUnEESiUCBp4GLThH9LOg"
    			+ "ZwDDOSYqFMxw8/JRVI0dDS2yMFoO913mjhTIK0Ml4/hkYzf3xDd0LsgOQO6H1ByVAr1Zx43q1Q4QeSE/8RFMSps9KKPknPfFMvZfOPpsSLnrQqe+fFPB25iKMeUc"
    			+ "vgUf/rvRAcdnI2pQKryrfIjFJAUFcjS5V5cqXBl5u+uVBiVk+eEqSSEB1834Wu+aVnf8OZ+QH47QcqhKrzBGMr5Uil/iTn8Squc/xY54beByNJY/LGzCrekuBUEl"
    			+ "kRmz1ssZIAyO0fkuFzLgJAn60S8M0zxISDj7WzaqjKFK7DSVyBYdfNsqlIXvYOVuzlkU5KzgZQKLXLPh+pGOSm+qOkDUl/W2bqXj541Qq1g2mhL5Tvg9agz0sDqP"
    			+ "hjjJyokATL1wo5Sp0rdXS7ICaLUbsq3j8BmBqfGbnWUG6IsvK7lfbJcwjZMpQJydp8hMiCu2QBdX9srfwk10hTXWpwAxYq8LnBixPLhyXKutwOuhpWDx+j7w/Pop"
    			+ "e8T7cB8rQin6mLaebhawUHPP9/q+hl3V77PyjxdV+j8PB5wnrkJjLLfjnAsng86HetLWUXf4zIV9TZT36fnUH48kxG6I4nSU8uxRMuBSA/vaDkWxHCd9fS7Uus6U"
    			+ "9upRqToYVkmIp12SVGKdeSDnc4+iNVz66pfxtYYZ4vLH1+psCCDsnbXH01rJ2uhVqpJTl121DJ0/4Fi9+VYxBeKpE2jKhCle2oHpCjFCAfxpQghpGHo2MCyP6wLI"
    			+ "YQQKpBQbCVlm4jneErVDkNBCCEkpwoEYRsuUH64irCMogIhhJDmUyAwwCLOESLUdg15jpf4KhBCSPMpEIDlgsP5OAkhhAokLFgeerjynR1toA2EEEKaXIHArwCOb"
    			+ "AhVYeNZTBsIIYQ0uQIBiHSK3As2OSqoPAghhAqkzJ26EEIIoQIJxUDlxxUK4gGOQgghhAqkEgR9RArYLQP2m0sFQgghVCCVYHkvfEQQXrxeytUX+SoQQggVSFs+U"
    			+ "UiHSgghhAokJMgfckyNkcgkXQghhFCB/B+YzkL49Utr/J8KhBBCqEBqUpKCRDz92vydBnRCCKECCeQqPnZCCKECiQK81X+if5+kOIVFCCFUIIZMlnK28pPTKyoQQ"
    			+ "gixo30T3zuCLg6SMouvASGEUIHYMk3KYOX7ihBCCLGgXUtLC2uBEEIIRyCEEEKoQAghhFCBEEIIoQIhhBBCqEAIIYRQgRBCCKECIYQQQgVCCCEkp/xXgAEAFR+3n"
    			+ "lt6iWYAAAAASUVORK5CYII=\">";
    }
}
