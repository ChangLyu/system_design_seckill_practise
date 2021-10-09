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

@RestController
@RequestMapping("/seckill")
public class SeckillController {

    @Autowired
    private SeckillService seckillService;

    @CrossOrigin
    @RequestMapping(value = "/list", method = RequestMethod.GET,produces = {"application/json; charset=UTF-8"})
    @ResponseBody
    public List<Seckill> list() {
        List<Seckill> list = seckillService.getSeckillList();
        return list;
    }

    @CrossOrigin
    @GetMapping(value = "/{seckillId}/detail")
    public SeckillResult<Seckill>  detail(@PathVariable("seckillId") Long seckillId, Model model) {
        SeckillResult<Seckill> result;
        try {
            Seckill seckill = seckillService.getById(seckillId);
            result = new SeckillResult<Seckill>(true, seckill);
        }catch(Exception e){
            e.printStackTrace();
            result = new SeckillResult<Seckill>(false, e.getMessage());
        }
        return result;
    }
    @CrossOrigin
    @GetMapping(value = "/{seckillId}/exposer")
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
    @CrossOrigin( allowCredentials = "true")
    @RequestMapping(value = "/{seckillId}/{md5}/execution", method = RequestMethod.POST, produces = {"application/json; charset=UTF-8"})
    @ResponseBody
    public SeckillResult<SeckillExecution> execute(@PathVariable("seckillId") Long seckillId, @PathVariable("md5") String md5,
                                                   @CookieValue(value = "userPhone", required = false) Long userPhone) {
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

    @CrossOrigin
    @RequestMapping(value = "/time/now", method = RequestMethod.GET,produces = {"application/json; charset=UTF-8"})
    @ResponseBody
    public SeckillResult<Long> time(){
        Date now = new Date();
        return new SeckillResult<Long>(true, now.getTime());
    }

}
