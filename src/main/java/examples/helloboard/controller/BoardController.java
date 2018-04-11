package examples.helloboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/boards")
public class BoardController {

    // http://localhost:8080/boards?page=2&searchType=content&searchStr=java
    @GetMapping
    public String boards(@RequestParam(name = "page", required = false, defaultValue = "1") int page,
                          @RequestParam(name = "searchType", required = false, defaultValue = "title") String searchType,
                          @RequestParam(name = "searchStr", required = false, defaultValue = "") String searchStr,
                          ModelMap modelMap
                          ){
        System.out.println("page :" + page);
        System.out.println("searchType :" + searchType);
        System.out.println("searchStr :" + searchStr);

        //TODO
        // searchType(title, content, titleAndContent), searchStr 이 있을 경우
        // 1. 검색된 결과의 총 수
        // 2. 한페이지에 보여줄 수
        // 1,2를 이용하여 페이징 처리에 해당하는 값을 구할 수 있다. 만약 전체가 97페이지면?
        // <<    <     1     2     3    4   5  >  >>    > 를 누르면 6페이지를 보여준다.
        // <<    <     6     7     8    9   10  >  >>
        // <<    <    96    97                  >  >>
        // 3. page에 해당하는 검색 결과 목록
        // searchType, searchStr 이 없을 경우
        return "boards_list";
    }

    @GetMapping("/{boardId}")
    public String boardRead(@PathVariable(value = "boardId")Long boardId,
                            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
                            @RequestParam(name = "searchType", required = false, defaultValue = "title") String searchType,
                            @RequestParam(name = "searchStr", required = false, defaultValue = "") String searchStr,
                            ModelMap modelMap){
        System.out.println("boardId : " + boardId);
        // 1. boardId에 해당하는 게시물을 한건 읽어온다.
        // 2. boardId의 이전 글, boardId의 이후 글. (검색어도 신경써라!!!)
        //    이전글 : 100보다 작으면서 가장 큰 boardId
        //    이후글 : 100보다 크면서 가장 작은 boardId
        return "boards_read";
    }

    public String boardWriteForm(){
        return "boards_writeform";
    }

    @PostMapping
    public String boardWrite(){
        return "redirect:/boards";
    }

    public String boardDeleteForm(){
        return "boards_deleteform";
    }

    @DeleteMapping
    public String boardDelete(){
        return "redirect:/boards";
    }

    public String boardUpdateForm(){
        return "boards_updateform";
    }

    @PutMapping
    public String boardUpdate(){
        return "redirect:/boards" + "/id";
    }
}


/*
/boards   boards_list         WEB-INF/views/board_list.jsp                   1page의 목록을 보여준다.
/boards   boards_list         WEB-INF/views/board_list.jsp    page=2         1page의 목록을 보여준다.
/boards   boards_list         WEB-INF/views/board_list.jsp    searchType=title&searchStr=apple         제목에서 apple로 검색한 1page의 목록을 보여준다.
/boards   boards_list         WEB-INF/views/board_list.jsp    page=3&searchType=title&searchStr=apple  제목에서 apple로 검색한      3page의 목록을 보여준다.

/boards/{boardId}   boards_read       WEB-INF/views/boards_read       page=3&searchType=title&searchStr=apple

/boards/writeform   boards_writeform   글쓰기폼이보여진다.         이름, 암호, 제목, 내용
/boards (POST)      boards 리다이렉트     글이 저장되고 리다이렉트된다.

/boards/deleteform/{boardId}  boards_deleteform  암호를 물어보는 폼이 보여진다.
/boards (DELETE)    boards 리다이렉트    글이 삭제되고 리다이렉트된다.             boardId=50&passwd=hello

/boards/updateform/{boardId}  boards_updateform
/boards (PUT)      boards/{boardId} 글이 수정되고 글상세보기로 리다이렉트한다.
 */