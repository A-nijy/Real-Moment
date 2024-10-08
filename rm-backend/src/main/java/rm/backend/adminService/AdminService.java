package rm.backend.adminService;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rm.backend.domain.entity.Admin;
import rm.backend.domain.entity.Member;
import rm.backend.domain.entity.RefreshToken;
import rm.backend.domain.repository.AdminRepository;
import rm.backend.domain.repository.MemberRepository;
import rm.backend.domain.repository.RefreshTokenRepository;
import rm.backend.dto.innerDto.AdminDto;
import rm.backend.dto.innerDto.SearchDto;
import rm.backend.jwt.config.util.JWTProperties;
import rm.backend.jwt.config.util.JwtFunction;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {

    private final MemberRepository memberRepository;
    private final AdminRepository adminRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtFunction jwtFunction;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JWTProperties jwtProperties;



    // 관리자 목록 조회
    @Transactional
    public AdminDto.PageResponse showAdmins(SearchDto.Admins request) {

        PageRequest pageRequest = PageRequest.of(request.getNowPage() - 1, 9);

        Page<Admin> admins = adminRepository.searchAdmins(request, pageRequest);

        List<AdminDto.Response> adminsDto = admins.stream()
                                                    .map(AdminDto.Response::new)
                                                    .collect(Collectors.toList());

        AdminDto.PageResponse adminPageDto = new AdminDto.PageResponse(adminsDto, admins.getTotalPages(), request.getNowPage());

        return adminPageDto;
    }


    @Transactional
    public AdminDto.Response showAdmin(Long adminId) {

        Admin admin = adminRepository.findById(adminId).orElseThrow(IllegalArgumentException::new);

        AdminDto.Response adminDto = new AdminDto.Response(admin);

        return adminDto;
    }


    @Transactional
    public AdminDto.Response updateAdmin(Long id, AdminDto.UpdateRequest request) {

        Admin admin = adminRepository.findById(id).orElseThrow(IllegalArgumentException::new);

        admin.update(request);

        AdminDto.Response adminDto = new AdminDto.Response(admin);

        return adminDto;
    }

    @Transactional
    public void roleUpdateAdmin(AdminDto.RoleUpdateRequest request) {

        if (!request.getRoles().equals("ROLE_OPERATOR") && !request.getRoles().equals("ROLE_CUSTOMER") && !request.getRoles().equals("ROLE_ADMIN") && !request.getRoles().equals("ROLE_REPRESENTATIVE")){
            throw new IllegalArgumentException("권한 입력이 잘못되었습니다.");
        }

        Admin admin = adminRepository.findById(request.getAdminId()).orElseThrow(IllegalArgumentException::new);

        admin.rolesUpdate(request);
    }


    @Transactional
    public void deleteAdmin(Long adminId) {

        if (adminId == 1){
            throw new IllegalArgumentException("대표의 아이디는 삭제가 불가능합니다.");
        }

        Admin admin = adminRepository.findById(adminId).orElseThrow(IllegalArgumentException::new);

        admin.delete();
    }


    @Transactional
    public AdminDto.Response myPageAdmin(Long adminId) {

        Admin admin = adminRepository.findById(adminId).orElseThrow(IllegalArgumentException::new);

        AdminDto.Response adminDto = new AdminDto.Response(admin);

        return adminDto;
    }


    // 아이디 중복 검사
    @Transactional
    public boolean idCheck(AdminDto.IdCheckRequest request) {

        Member member = memberRepository.findByLoginId(request.getLoginId()).orElse(null);
        Admin admin = adminRepository.findByLoginId(request.getLoginId()).orElse(null);

        return member == null && admin == null;
    }


    // 관리자 가입
    @Transactional
    public void createAdmin(AdminDto.CreateRequest request) {

        Member memberId = memberRepository.findByLoginId(request.getLoginId()).orElse(null);
        Admin adminId = adminRepository.findByLoginId(request.getLoginId()).orElse(null);

        if(!(memberId == null && adminId == null)){
            throw new IllegalArgumentException("아이디가 중복입니다.");
        }

        request.setLoginPassword(bCryptPasswordEncoder.encode(request.getLoginPassword()));
        request.setRoles("ROLE_ADMIN");

        Admin admin = new Admin(request);

        adminRepository.save(admin);
    }


    // 관리자 로그인
    @Transactional
    public AdminDto.AdminIdResponse adminLogin(AdminDto.LoginRequest request, HttpServletResponse response) {

        Admin admin = adminRepository.findByLoginId(request.getLoginId()).orElseThrow(IllegalArgumentException::new);

        if(!bCryptPasswordEncoder.matches(request.getLoginPassword(), admin.getLoginPassword())){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String accessToken = jwtFunction.createAccessToken(admin);
        String refreshToken = jwtFunction.createRefreshToken(admin);

        RefreshToken checkRefreshTooken = refreshTokenRepository.findByLoginId(admin.getLoginId()).orElse(null);

        if(checkRefreshTooken != null){
            checkRefreshTooken.updateToken(refreshToken);
        } else {
            RefreshToken refreshTokenEntity = new RefreshToken(admin.getLoginId(), refreshToken);
            refreshTokenRepository.save(refreshTokenEntity);
        }

        response.addHeader(jwtProperties.getACCESS_STRING(), accessToken);
        response.addHeader(jwtProperties.getREFRESH_STRING(), refreshToken);

        AdminDto.AdminIdResponse adminId = new AdminDto.AdminIdResponse(admin.getAdminId());

        return adminId;
    }


    // 관리자 로그아웃
    @Transactional
    public void adminLogout(Long id) {

        Admin admin = adminRepository.findById(id).orElseThrow(IllegalArgumentException::new);

        RefreshToken refreshToken = refreshTokenRepository.findByLoginId(admin.getLoginId()).orElseThrow(IllegalArgumentException::new);

        refreshTokenRepository.delete(refreshToken);
    }


    // access 토큰 재발급
    @Transactional
    public void reissueAccessToken(HttpServletRequest request, HttpServletResponse response) {

        String header_refresh = request.getHeader(jwtProperties.getREFRESH_STRING());

        RefreshToken refresh = refreshTokenRepository.findByToken(header_refresh).orElseThrow(() -> new IllegalArgumentException("해당 refresh토큰이 DB에 존재하지 않습니다."));

        Admin admin = adminRepository.findByLoginId(refresh.getLoginId()).orElseThrow(() -> new IllegalArgumentException("해당 회원은 존재하지 않습니다."));

        String accessToken = jwtFunction.createAccessToken(admin);
        String refreshToken = jwtFunction.createRefreshToken(admin);

        refresh.updateToken(refreshToken);

        response.addHeader(jwtProperties.getACCESS_STRING(), accessToken);
        response.addHeader(jwtProperties.getREFRESH_STRING(), refreshToken);
    }


}
