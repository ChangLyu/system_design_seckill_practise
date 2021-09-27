package seckill.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import seckill.dto.Exposer;
import seckill.dto.SeckillExecution;
import seckill.dto.SeckillResult;
import seckill.entity.Seckill;
import seckill.enums.SeckillStatEnum;
import seckill.exception.RepeatKillException;
import seckill.exception.SeckillCloseException;
import seckill.service.SeckillService;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/seckill")
public class SeckillController {

    @Autowired
    private SeckillService seckillService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model) {
        //list.jsp+mode=ModelAndView
        //获取列表页
        List<Seckill> list = seckillService.getSeckillList();
        model.addAttribute(list);
        return "list"; // WEB-INFO/jsp/list.jsp
    }

    @RequestMapping(value = "/{seckillId}/detail", method = RequestMethod.GET)
    public String detail(@PathVariable("seckillId") Long seckillId, Model model) {
        if (seckillId == null) {
            return "redirect:/seckill/list";
        }

        Seckill seckill = seckillService.getById(seckillId);
        if (seckill == null) {
            return "forward:/seckill/list";
        }

        model.addAttribute("seckill", seckill);

        return "detail";
    }

    @RequestMapping(value = "{/seckillId}/exposer", method = RequestMethod.GET, produces = {"application/json; charset=UTF-8"})
    @ResponseBody
    public SeckillResult<Exposer> exposer(@PathVariable("seckillId") Long seckillId) {
        SeckillResult<Exposer> result;
        try {
            Exposer exposer = seckillService.exportSeckillUrl(seckillId);
            result = new SeckillResult<Exposer>(true, exposer);
        } catch (Exception e) {
            e.printStackTrace();
            result = new SeckillResult<Exposer>(false, e.getMessage());
        }
        return result;
    }

    /*Service层中的抛出异常是为了让Spring能够回滚，Controller层中捕获异常是为了将异常转换为对应的Json供前台使用，缺一不可。*/
    @RequestMapping(value = "{/seckillId}/{md5}/execution}", method = RequestMethod.POST, produces = {"application/json; charset=UTF-8"})
    @ResponseBody
    public SeckillResult<SeckillExecution> execute(@PathVariable("seckillId") Long seckillId, @PathVariable("md5") String md5,
                                                   @CookieValue(value = "killedPhone", required = false) Long userPhone) {
        SeckillResult<SeckillExecution> seckillResult;
        if (userPhone == null) {
            seckillResult = new SeckillResult<SeckillExecution>(false, "User unregistered!");
        }
        try {
            SeckillExecution seckillExecution = seckillService.executeSeckill(seckillId, userPhone, md5);
            seckillResult = new SeckillResult<SeckillExecution>(true, seckillExecution);
        } catch (RepeatKillException e) {
            seckillResult = new SeckillResult<SeckillExecution>(false, new SeckillExecution(seckillId, SeckillStatEnum.REPEAT_KILL));
        } catch (SeckillCloseException e) {
            seckillResult = new SeckillResult<SeckillExecution>(false, new SeckillExecution(seckillId, SeckillStatEnum.END));
        } catch (Exception e) {
            e.printStackTrace();
            seckillResult = new SeckillResult<SeckillExecution>(false, new SeckillExecution(seckillId, SeckillStatEnum.INNER_ERROR));
        }
        return seckillResult;
    }

    @RequestMapping(value = "/time/now", method = RequestMethod.GET)
    @ResponseBody
    public SeckillResult<Long> time(){
        Date now = new Date();
        return new SeckillResult<Long>(true, now.getTime());
    }

}
