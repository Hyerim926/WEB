package article.service;

import java.util.List;

import article.model.Article;

// 앞서, 전체 게시글 개수와 원하는 범위의 게시글 목록을 읽어오는 기능을 추가했으므로,
// 이제 게시글 목록을 제공하는 서비스 클래스를 구현할 차례이다.
// 먼저 게시글 데이터와 페이징 관련 정보를 담을 ArticlePage 클래스를 구현한다.
public class ArticlePage {
	
	// total = 전체 게시글의 개수를 보관한다.
	private int total;
	// currentPage는 사용자가 요청한 페이지 번호를 보관한다.
	private int currentPage;
	// content는 화면에 출력할 게시글 목록을 보관한다.
	private List<Article> content;
	// totalPages는 전체 페이지 개수를 보관한다.
	private int totalPages;
	// 화면 하단에 보여줄 페이징 처리 부분의 페이지 이동 링크의 시작 번호를 저장한다.
	private int startPage;
	// 화면 하단에 보여줄 페이징 처리 부분의 페이지 이동 링크의 끝 번호를 저장한다.
	private int endPage;
	
	public ArticlePage(int total, int currentPage, int size, List<Article> content) {
		this.total = total;
		this.currentPage = currentPage;
		this.content = content;
		// 만약에 total 게시글 개수가 0이면,
		// totalPages, startpage, endPage를 모두 0으로 할당한다.
		if(total == 0) {
			totalPages = 0;
			startPage = 0;
			endPage = 0;
		} else {
			// total 게시글 개수를 이용하여 저체 페이지 개수를 구한다.
			// 한 페이지에 보여줄 게시글 개수를 size로 전달 받는다.
			// 게시글 개수를 size로 나눈 값을 페이지 개수로 사용한다.
			totalPages = total/size;
			// 만약에, 전체 게시글 개수를 size로 나눈 나머지가
			// 0보다 크면 페이지 수를 1 증가한다.
			if(total % size > 0) {
				totalPages++;
			}
			// 예를들어, 전체 게시글 개수가 34이고, size가 10이라고 한다면,
			// 이 경우 34/10은 3이고(정수/정수는 정수이다)
			// 34 % 10은 4이다. 따라서 전체 페이지 개수는
			// tatalPages = total/size; 결과인 3에
			// if(total % size > 0) { totalPages++ ;} 의 결과로
			// 1을 증가한 4가 된다.
			// 게시글 개수가 60이면 60/10은 6이고,
			// 60%10은 0이므로, 전체 페이지 개수는 6이 된다.
			
			// 화면 하단에 보여줄 페이지 이동 링크(페이징 처리 부분)의
			// 시작 페이지 번호를 구한다.
			// 화면 하단에 [1, 2, 3, 4, 5]나 [6, 7, 8, 9, 10]처럼
			// 5개 페이지씩 이동 링크를 출력한다가 가정하면,
			// 이 경우, 현재 페이지가 3이면 시작 페이지가 1이 되고,
			// 현재 페이지가 10이면 시작 페이지가 6이 된다.
			int modVal = currentPage % 5;
			startPage = currentPage / 5 * 5 + 1;
			// if(modVal == 0) { startPage = startPage - 5; } 
			if(modVal == 0) {
				startPage -= 5;
			}
			
			// 화면 하단에 보여줄 페이지 이동 링크의 끝 페이지 번호를 구한다.
			// 화면 하단에 5개 페이지씩 이동 링크를 출력하므로
			// 시작 페이지 번호에 4를 추가한 값이 끝 페이지 번호이다.
			endPage = startPage + 4;
			if(endPage > totalPages) {
				endPage = totalPages;
			}
		}
	}

	public int getTotal() {
		return total;
	}
	
	public boolean hasNoArticles() {
		return total == 0;
	}
	
	public boolean hasArticles() {
		return total > 0;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public List<Article> getContent() {
		return content;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public int getStartPage() {
		return startPage;
	}

	public int getEndPage() {
		return endPage;
	}
	
}
